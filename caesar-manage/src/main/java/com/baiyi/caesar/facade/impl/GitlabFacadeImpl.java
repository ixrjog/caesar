package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.builder.gitlab.GitlabBaseBranchBuilder;
import com.baiyi.caesar.builder.gitlab.GitlabGroupBuilder;
import com.baiyi.caesar.builder.gitlab.GitlabProjectBuilder;
import com.baiyi.caesar.builder.gitlab.GitlabWebhookBuilder;
import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.base.GitlabEventType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.GitlabUtils;
import com.baiyi.caesar.common.util.IDUtils;
import com.baiyi.caesar.consumer.GitlabWebhooksConsumer;
import com.baiyi.caesar.convert.GitlabBranchConvert;
import com.baiyi.caesar.decorator.gitlab.GitlabGroupDecorator;
import com.baiyi.caesar.decorator.gitlab.GitlabInstanceDecorator;
import com.baiyi.caesar.decorator.gitlab.GitlabProjectDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import com.baiyi.caesar.domain.vo.gitlab.*;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.facade.EnvFacade;
import com.baiyi.caesar.facade.GitlabFacade;
import com.baiyi.caesar.facade.TagFacade;
import com.baiyi.caesar.factory.gitlab.GitlabEventHandlerFactory;
import com.baiyi.caesar.factory.gitlab.IGitlabEventHandler;
import com.baiyi.caesar.gitlab.handler.GitlabBranchHandler;
import com.baiyi.caesar.gitlab.handler.GitlabGroupHandler;
import com.baiyi.caesar.gitlab.handler.GitlabProjectHandler;
import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.gitlab.CsGitlabGroupService;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabBranchCommit;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
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
    private CsGitlabGroupService csGitlabGroupService;

    @Resource
    private GitlabInstanceDecorator gitlabInstanceDecorator;

    @Resource
    private GitlabProjectDecorator gitlabProjectDecorator;

    @Resource
    private GitlabGroupDecorator gitlabGroupDecorator;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private GitlabProjectHandler gitlabProjectHandler;

    @Resource
    private GitlabGroupHandler gitlabGroupHandler;

    @Resource
    private GitlabBranchHandler gitlabBranchHandler;

    @Resource
    private GitlabServerContainer gitlabServerContainer;

    @Resource
    private CsGitlabWebhookService csGitlabWebhookService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private TagFacade tagFacade;

    @Resource
    private GitlabWebhooksConsumer gitlabWebhooksConsumer;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private EnvFacade envFacade;

    public static final String[] DEFAULT_DEPLOY_BRANCHES = {"dev", "daily", "gray"};

    public static final String DEFAULT_DEF_BRANCH = "master";

    private BusinessWrapper<GitlabBranchVO.BaseBranch> queryGitlabProjectBranchCommit(int id, String branch) {
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(id);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(csGitlabProject.getInstanceId());
        try {
            GitlabBranch gitlabBranch = gitlabBranchHandler.getBranch(csGitlabInstance.getName(), csGitlabProject.getProjectId(), branch);
            GitlabBranchCommit gitlabBranchCommit = gitlabBranch.getCommit();
            return new BusinessWrapper<>(GitlabBaseBranchBuilder.build(csGitlabProject, gitlabBranchCommit, branch));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BusinessWrapper<>(ErrorEnum.GITLAB_BRANCH_COMMIT_ERROR);
    }

    @Override
    public DataTable<GitlabInstanceVO.Instance> queryGitlabInstancePage(GitlabInstanceParam.PageQuery pageQuery) {
        DataTable<CsGitlabInstance> table = csGitlabInstanceService.queryCsGitlabInstanceByParam(pageQuery);
        List<GitlabInstanceVO.Instance> page = BeanCopierUtils.copyListProperties(table.getData(), GitlabInstanceVO.Instance.class);
        return new DataTable<>(page.stream().map(e -> gitlabInstanceDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void webhooksV1(GitlabHooksVO.Webhook webhook) {
        try {
            // 处理push事件
            if (webhook.getEvent_name().equals(GitlabEventType.PUSH.getDesc())) {
                saveWebhooks(webhook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void systemHooksV1(GitlabHooksVO.SystemHook systemHook) {
        // 处理系统消息
        try {
            IGitlabEventHandler iGitlabEventHandler = GitlabEventHandlerFactory.getGitlabEventHanlderByKey(systemHook.getEventName());
            if (iGitlabEventHandler != null)
                iGitlabEventHandler.consumeEvent(systemHook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveWebhooks(GitlabHooksVO.Webhook webhooks) {
        CsGitlabInstance instance = filter(csGitlabInstanceService.queryAll(), webhooks);
        if (instance == null)
            return;
        OcUser ocUser = ocUserService.queryOcUserByUsername(webhooks.getUser_username());
        CsGitlabWebhook csGitlabWebhook = GitlabWebhookBuilder.build(webhooks, instance, ocUser);
        csGitlabWebhookService.addCsGitlabWebhook(csGitlabWebhook);
        gitlabWebhooksConsumer.consumerWebhooks(csGitlabWebhook);
    }

    private CsGitlabInstance filter(List<CsGitlabInstance> instances, GitlabHooksVO.Webhook webhooks) {
        for (CsGitlabInstance instance : instances) {
            try {
                java.net.URL instanceUrl = new java.net.URL(instance.getUrl());
                java.net.URL webhooksUrl = new java.net.URL(webhooks.getProject().getWeb_url());
                if (instanceUrl.getHost().equals(webhooksUrl.getHost()))
                    return instance;
            } catch (MalformedURLException ignored) {
            }
        }
        return null;
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
    public DataTable<GitlabProjectVO.Project> queryGitlabProjectPage(GitlabProjectParam.GitlabProjectPageQuery pageQuery) {
        DataTable<CsGitlabProject> table = csGitlabProjectService.queryCsGitlabProjectByParam(pageQuery);
        List<GitlabProjectVO.Project> page = BeanCopierUtils.copyListProperties(table.getData(), GitlabProjectVO.Project.class);
        return new DataTable<>(page.stream().map(e -> gitlabProjectDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<GitlabGroupVO.Group> queryGitlabGroupPage(GitlabGroupParam.GitlabGroupPageQuery pageQuery) {
        DataTable<CsGitlabGroup> table = csGitlabGroupService.queryCsGitlabGroupByParam(pageQuery);
        List<GitlabGroupVO.Group> page = BeanCopierUtils.copyListProperties(table.getData(), GitlabGroupVO.Group.class);
        return new DataTable<>(page.stream().map(e -> gitlabGroupDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void syncGitlabInstanceProject(int instanceId) {
        Map<Integer, CsGitlabProject> projectMap = getGitlabProjectMap(instanceId);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(instanceId);
        List<GitlabProject> gitlabProjects = gitlabProjectHandler.getProjects(csGitlabInstance.getName());
        if (CollectionUtils.isEmpty(gitlabProjects)) return;
        gitlabProjects.forEach(e -> saveGitlabProject(csGitlabInstance, e, projectMap));
        deleteGitlabProjectByMap(projectMap); // 删除不存在的项目
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_COMMON)
    public void syncGitlabInstanceGroup(int instanceId) {
        Map<Integer, CsGitlabGroup> groupMap = getGitlabGroupMap(instanceId);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(instanceId);
        try {
            List<GitlabGroup> gitlabGroups = gitlabGroupHandler.getGroups(csGitlabInstance.getName());
            if (CollectionUtils.isEmpty(gitlabGroups)) return;
            gitlabGroups.forEach(e -> saveGitlabGroup(csGitlabInstance, e, groupMap));
            deleteGitlabGroupByMap(groupMap); // 删除不存在的项目
        } catch (IOException ignored) {
        }
    }

    @Override
    public void saveGitlabProject(CsGitlabInstance csGitlabInstance, GitlabProject gitlabProject, Map<Integer, CsGitlabProject> projectMap) {
        CsGitlabProject pre = GitlabProjectBuilder.build(csGitlabInstance, gitlabProject);
        if (projectMap.containsKey(pre.getProjectId())) {
            CsGitlabProject csGitlabProject = projectMap.get(pre.getProjectId());
            pre.setId(csGitlabProject.getId());
            csGitlabProjectService.updateCsGitlabProject(pre);
            projectMap.remove(pre.getProjectId());
        } else {
            csGitlabProjectService.addCsGitlabProject(pre);
        }
        applicationFacade.updateApplicationScmMember(pre);
    }

    @Override
    public void saveGitlabGroup(CsGitlabInstance csGitlabInstance, GitlabGroup gitlabGroup, Map<Integer, CsGitlabGroup> groupMap) {
        CsGitlabGroup pre = GitlabGroupBuilder.build(csGitlabInstance, gitlabGroup);
        if (groupMap.containsKey(pre.getGroupId())) {
            CsGitlabGroup csGitlabGroup = groupMap.get(pre.getGroupId());
            pre.setId(csGitlabGroup.getId());
            pre.setApplicationKey(csGitlabGroup.getApplicationKey());
            csGitlabGroupService.updateCsGitlabGroup(pre);
            groupMap.remove(pre.getGroupId());
        } else {
            csGitlabGroupService.addCsGitlabGroup(pre);
        }
    }

    private void deleteGitlabProjectByMap(Map<Integer, CsGitlabProject> projectMap) {
        if (projectMap.isEmpty()) return;
        projectMap.keySet().parallelStream().forEach(k -> {
            CsGitlabProject csGitlabProject = projectMap.get(k);
            // 清除业务标签
            tagFacade.clearBusinessTags(BusinessType.GITLAB_PROJECT.getType(), csGitlabProject.getId());
            csGitlabProjectService.deleteCsGitlabProjectById(csGitlabProject.getId());
        });
    }

    private void deleteGitlabGroupByMap(Map<Integer, CsGitlabGroup> groupMap) {
        if (groupMap.isEmpty()) return;
        groupMap.keySet().parallelStream().forEach(k -> {
            CsGitlabGroup csGitlabGroup = groupMap.get(k);
            // 清除业务标签
            tagFacade.clearBusinessTags(BusinessType.GITLAB_GROUP.getType(), csGitlabGroup.getId());
            csGitlabGroupService.deleteCsGitlabGroupById(csGitlabGroup.getId());
        });
    }

    private Map<Integer, CsGitlabProject> getGitlabProjectMap(int instanceId) {
        List<CsGitlabProject> projects = csGitlabProjectService.queryCsGitlabProjectByInstanceId(instanceId);
        if (CollectionUtils.isEmpty(projects)) return Maps.newHashMap();
        return projects.stream().collect(Collectors.toMap(CsGitlabProject::getProjectId, a -> a, (k1, k2) -> k1));
    }

    private Map<Integer, CsGitlabGroup> getGitlabGroupMap(int instanceId) {
        List<CsGitlabGroup> groups = csGitlabGroupService.queryCsGitlabGroupByInstanceId(instanceId);
        if (CollectionUtils.isEmpty(groups)) return Maps.newHashMap();
        return groups.stream().collect(Collectors.toMap(CsGitlabGroup::getGroupId, a -> a, (k1, k2) -> k1));
    }

    @Override
    public BusinessWrapper<GitlabBranchVO.Repository> queryApplicationSCMMemberBranch(ApplicationParam.ScmMemberBranchQuery scmMemberBranchQuery) {
        CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(scmMemberBranchQuery.getScmMemberId());
        if (csApplicationScmMember == null)
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SCM_NOT_EXIST);
        CsApplication csApplication = csApplicationService.queryCsApplicationById(csApplicationScmMember.getApplicationId());
        String envName = null;
        if (csApplication.getEnableGitflow() && !IDUtils.isEmpty(scmMemberBranchQuery.getCiJobId())) {
            CsCiJob csCiJob = csCiJobService.queryCsCiJobById(scmMemberBranchQuery.getCiJobId());
            envName = envFacade.queryEnvNameByType(csCiJob.getEnvType());
        }
        return queryGitlabProjectRepository(csApplicationScmMember.getScmId(), scmMemberBranchQuery.getEnableTag(), csApplication.getEnableGitflow(), envName);
    }

    @Override
    public BusinessWrapper<Boolean> createApplicationSCMMemberBranch(ApplicationParam.CreateScmMemberBranch createParam) {
        CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(createParam.getScmMemberId());
        if (csApplicationScmMember == null)
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SCM_NOT_EXIST);
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(csApplicationScmMember.getScmId());
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(csGitlabProject.getInstanceId());
        for (String branch : DEFAULT_DEPLOY_BRANCHES) {
            GitlabBranch gitlabBranch = null;
            try {
                gitlabBranch = gitlabBranchHandler.getBranch(csGitlabInstance.getName(), csGitlabProject.getProjectId(), branch);
                // FileNotFoundException
            } catch (IOException ignored) {
            }
            try {
                if (gitlabBranch == null)
                    gitlabBranchHandler.createBranch(csGitlabInstance.getName(), csGitlabProject.getProjectId(), branch, DEFAULT_DEF_BRANCH);
            } catch (IOException ignored) {
            }
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<GitlabBranchVO.BaseBranch> queryApplicationSCMMemberBranchCommit(ApplicationParam.ScmMemberBranchCommitQuery query) {
        CsApplicationScmMember csApplicationScmMember = csApplicationScmMemberService.queryCsApplicationScmMemberById(query.getScmMemberId());
        if (csApplicationScmMember == null)
            return new BusinessWrapper<>(ErrorEnum.APPLICATION_SCM_NOT_EXIST);
        return queryGitlabProjectBranchCommit(csApplicationScmMember.getScmId(), query.getBranch());
    }

    private BusinessWrapper<GitlabBranchVO.Repository> queryGitlabProjectRepository(int id, boolean enableTag, boolean enableGitflow, String envName) {
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(id);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(csGitlabProject.getInstanceId());
        List<GitlabBranchVO.BaseBranch> branches = GitlabBranchConvert.convertBranches(gitlabBranchHandler.getBranches(csGitlabInstance.getName(), csGitlabProject.getProjectId()));
        if (enableGitflow && !StringUtils.isEmpty(envName))
            branches = branches.stream().filter(e ->
                    GitlabUtils.filterBranchByGitflow(envName, e)).collect(Collectors.toList());
        GitlabBranchVO.Repository repository = new GitlabBranchVO.Repository();
        List<GitlabBranchVO.Option> options = Lists.newArrayList();
        options.add(GitlabBranchConvert.build("Branches", branches));
        if (enableTag) {
            List<GitlabBranchVO.BaseBranch> tags = GitlabBranchConvert.convertTags(gitlabBranchHandler.getTags(csGitlabInstance.getName(), csGitlabProject.getProjectId()));
            options.add(GitlabBranchConvert.build("Tags", tags));
        }
        repository.setOptions(options);
        return new BusinessWrapper<>(repository);
    }

}