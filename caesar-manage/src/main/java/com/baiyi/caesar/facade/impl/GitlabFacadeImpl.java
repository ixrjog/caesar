package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.builder.GitlabProjectBuilder;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.convert.GitlabBranchConvert;
import com.baiyi.caesar.decorator.gitlab.GitlabInstanceDecorator;
import com.baiyi.caesar.decorator.gitlab.GitlabProjectDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabProjectVO;
import com.baiyi.caesar.facade.GitlabFacade;
import com.baiyi.caesar.facade.TagFacade;
import com.baiyi.caesar.gitlab.handler.GitlabBranchHandler;
import com.baiyi.caesar.gitlab.handler.GitlabProjectHandler;
import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.gitlab.api.models.GitlabProject;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.caesar.common.base.Global.ASYNC_POOL_TASK_COMMON;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:42 上午
 * @Version 1.0
 */
@Service
public class GitlabFacadeImpl implements GitlabFacade {

    @Resource
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private GitlabInstanceDecorator gitlabInstanceDecorator;

    @Resource
    private GitlabProjectDecorator gitlabProjectDecorator;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private GitlabProjectHandler gitlabProjectHandler;

    @Resource
    private GitlabBranchHandler gitlabBranchHandler;

    @Resource
    private GitlabServerContainer gitlabServerContainer;

    @Resource
    private TagFacade tagFacade;

    @Override
    public DataTable<GitlabInstanceVO.Instance> queryGitlabInstancePage(GitlabInstanceParam.PageQuery pageQuery) {
        DataTable<CsGitlabInstance> table = csGitlabInstanceService.queryCsGitlabInstanceByParam(pageQuery);
        List<GitlabInstanceVO.Instance> page = BeanCopierUtils.copyListProperties(table.getData(), GitlabInstanceVO.Instance.class);
        return new DataTable<>(page.stream().map(e -> gitlabInstanceDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addGitlabInstance(GitlabInstanceVO.Instance instance) {
        CsGitlabInstance csGitlabInstance = BeanCopierUtils.copyProperties(instance, CsGitlabInstance.class);
        csGitlabInstance.setToken(stringEncryptor.encrypt(instance.getToken()));
        csGitlabInstanceService.addCsGitlabInstance(csGitlabInstance);
        gitlabServerContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateGitlabInstance(GitlabInstanceVO.Instance instance) {
        CsGitlabInstance pre = BeanCopierUtils.copyProperties(instance, CsGitlabInstance.class);
        if (StringUtils.isEmpty(pre.getToken())) {
            CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(instance.getId());
            pre.setToken(csGitlabInstance.getToken());
        } else {
            pre.setToken(stringEncryptor.encrypt(instance.getToken()));
        }
        csGitlabInstanceService.updateCsGitlabInstance(pre);
        gitlabServerContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteGitlabInstanceById(int id) {
        csGitlabInstanceService.deleteCsGitlabInstanceById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<GitlabProjectVO.Project> queryGitlabProjectPage(GitlabProjectParam.PageQuery pageQuery) {
        DataTable<CsGitlabProject> table = csGitlabProjectService.queryCsGitlabProjectByParam(pageQuery);
        List<GitlabProjectVO.Project> page = BeanCopierUtils.copyListProperties(table.getData(), GitlabProjectVO.Project.class);
        return new DataTable<>(page.stream().map(e -> gitlabProjectDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void syncGitlabInstanceProject(int instanceId) {
        Map<Integer, CsGitlabProject> projectMap = getGitlabProjectMap(instanceId);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(instanceId);
        List<GitlabProject> gitlabProjects = gitlabProjectHandler.getProjects(csGitlabInstance.getName());
        if (CollectionUtils.isEmpty(gitlabProjects)) return;
        gitlabProjects.forEach(e -> saveGitlabProject(instanceId, e, projectMap));
        deleteGitlabProjectByMap(projectMap); // 删除不存在的项目
    }

    private void saveGitlabProject(Integer instanceId, GitlabProject gitlabProject, Map<Integer, CsGitlabProject> projectMap) {
        CsGitlabProject pre = GitlabProjectBuilder.build(instanceId, gitlabProject);
        if (projectMap.containsKey(pre.getProjectId())) {
            CsGitlabProject csGitlabProject = projectMap.get(pre.getProjectId());
            pre.setId(csGitlabProject.getId());
            csGitlabProjectService.updateCsGitlabProject(pre);
            projectMap.remove(pre.getProjectId());
        } else {
            csGitlabProjectService.addCsGitlabProject(pre);
        }
    }

    private void deleteGitlabProjectByMap(Map<Integer, CsGitlabProject> projectMap) {
        if (projectMap.isEmpty()) return;
        projectMap.keySet().forEach(k -> {
            CsGitlabProject csGitlabProject = projectMap.get(k);
            // 清除业务标签
            tagFacade.clearBusinessTags(BusinessType.GITLAB_PROJECT.getType(), csGitlabProject.getId());
            csGitlabProjectService.deleteCsGitlabProjectById(csGitlabProject.getId());
        });
    }

    private Map<Integer, CsGitlabProject> getGitlabProjectMap(int instanceId) {
        List<CsGitlabProject> projects = csGitlabProjectService.queryCsGitlabProjectByInstanceId(instanceId);
        if (CollectionUtils.isEmpty(projects)) return Maps.newHashMap();
        return projects.stream().collect(Collectors.toMap(CsGitlabProject::getProjectId, a -> a, (k1, k2) -> k1));
    }

    @Override
    public BusinessWrapper<GitlabBranchVO.Repository> queryGitlabProjectRepository(int id, boolean enableTag) {
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(id);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(csGitlabProject.getInstanceId());
        List<GitlabBranchVO.BaseBranch> branches = GitlabBranchConvert.convertBranches(gitlabBranchHandler.getBranches(csGitlabInstance.getName(), csGitlabProject.getProjectId()));
        GitlabBranchVO.Repository repository = new GitlabBranchVO.Repository();
        List<GitlabBranchVO.Option> options = Lists.newArrayList();
        options.add(GitlabBranchConvert.build("Branches",branches));
        if (enableTag) {
            List<GitlabBranchVO.BaseBranch> tags = GitlabBranchConvert.convertTags(gitlabBranchHandler.getTags(csGitlabInstance.getName(), csGitlabProject.getProjectId()));
            options.add(GitlabBranchConvert.build("Tags",tags));
        }
        repository.setOptions( options);
        return new BusinessWrapper<>(repository);

    }

}
