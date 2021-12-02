package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.builder.gitlab.GitlabBaseBranchBuilder;
import com.baiyi.caesar.builder.gitlab.GitlabGroupBuilder;
import com.baiyi.caesar.builder.gitlab.GitlabProjectBuilder;
import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.GitlabUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.convert.GitlabBranchConvert;
import com.baiyi.caesar.packer.gitlab.GitalbEventWarp;
import com.baiyi.caesar.packer.gitlab.GitlabGroupDecorator;
import com.baiyi.caesar.packer.gitlab.GitlabInstanceDecorator;
import com.baiyi.caesar.packer.gitlab.GitlabProjectDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabEventParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import com.baiyi.caesar.domain.vo.gitlab.*;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.facade.EnvFacade;
import com.baiyi.caesar.facade.GitlabFacade;
import com.baiyi.caesar.facade.TagFacade;
import com.baiyi.caesar.factory.gitlab.systemhook.ISystemHookEventConsume;
import com.baiyi.caesar.factory.gitlab.systemhook.SystemHookEventConsumeFactory;
import com.baiyi.caesar.factory.gitlab.webhook.GitlabEventConsumeFactory;
import com.baiyi.caesar.factory.gitlab.webhook.IGitlabEventConsume;
import com.baiyi.caesar.gitlab.handler.GitlabBranchHandler;
import com.baiyi.caesar.gitlab.handler.GitlabGroupHandler;
import com.baiyi.caesar.gitlab.handler.GitlabProjectHandler;
import com.baiyi.caesar.gitlab.handler.GitlabUserHandler;
import com.baiyi.caesar.gitlab.server.GitlabServerContainer;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.service.gitlab.CsGitlabGroupService;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.gitlab.api.models.*;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private TagFacade tagFacade;

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private GitlabUserHandler gitlabUserHandler;

    @Resource
    private EnvFacade envFacade;

    @Resource
    private GitalbEventWarp gitalbEventWarp;

    @Resource
    private CsGitlabWebhookService gitlabWebhookService;

    public static final String[] DEFAULT_DEPLOY_BRANCHES = {"dev", "daily", "gray"};

    public static final String DEFAULT_REF_BRANCH = "master";

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
        List<GitlabInstanceVO.Instance> page = BeanCopierUtil.copyListProperties(table.getData(), GitlabInstanceVO.Instance.class);
        return new DataTable<>(page.stream().map(e -> gitlabInstanceDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Async(value = Global.TaskPools.COMMON)
    public void webhooksV1(GitlabHookVO.Webhook webhook) {
        try {
            IGitlabEventConsume eventConsume = GitlabEventConsumeFactory.getEventConsumeByKey(webhook.getObject_kind());
            if (eventConsume != null)
                eventConsume.consumeEvent(webhook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void systemHooksV1(GitlabHookVO.SystemHook systemHook) {
        // 处理系统消息
        try {
            ISystemHookEventConsume eventConsume = SystemHookEventConsumeFactory.getSystemHookEventConsumeByKey(systemHook.getEventName());
            if (eventConsume != null)
                eventConsume.consumeEvent(systemHook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BusinessWrapper<Boolean> addGitlabInstance(GitlabInstanceVO.Instance instance) {
        CsGitlabInstance csGitlabInstance = BeanCopierUtil.copyProperties(instance, CsGitlabInstance.class);
        csGitlabInstance.setToken(stringEncryptor.encrypt(instance.getToken()));
        csGitlabInstanceService.addCsGitlabInstance(csGitlabInstance);
        gitlabServerContainer.reset();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateGitlabInstance(GitlabInstanceVO.Instance instance) {
        CsGitlabInstance pre = BeanCopierUtil.copyProperties(instance, CsGitlabInstance.class);
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
        List<GitlabProjectVO.Project> page = BeanCopierUtil.copyListProperties(table.getData(), GitlabProjectVO.Project.class);
        return new DataTable<>(page.stream().map(e -> gitlabProjectDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<GitlabGroupVO.Group> queryGitlabGroupPage(GitlabGroupParam.GitlabGroupPageQuery pageQuery) {
        DataTable<CsGitlabGroup> table = csGitlabGroupService.queryCsGitlabGroupByParam(pageQuery);
        List<GitlabGroupVO.Group> page = BeanCopierUtil.copyListProperties(table.getData(), GitlabGroupVO.Group.class);
        return new DataTable<>(page.stream().map(e -> gitlabGroupDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Async(value = Global.TaskPools.COMMON)
    public void syncGitlabInstanceProject(int instanceId) {
        Map<Integer, CsGitlabProject> projectMap = getGitlabProjectMap(instanceId);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(instanceId);
        List<GitlabProject> gitlabProjects = gitlabProjectHandler.getProjects(csGitlabInstance.getName());
        if (CollectionUtils.isEmpty(gitlabProjects)) return;
        gitlabProjects.parallelStream().forEach(e -> saveGitlabProject(csGitlabInstance, e, projectMap));
        deleteGitlabProjectByMap(projectMap); // 删除不存在的项目
    }

    @Override
    @Async(value = Global.TaskPools.COMMON)
    public void syncGitlabInstanceGroup(int instanceId) {
        Map<Integer, CsGitlabGroup> groupMap = getGitlabGroupMap(instanceId);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(instanceId);
        try {
            List<GitlabGroup> gitlabGroups = gitlabGroupHandler.getGroups(csGitlabInstance.getName());
            if (CollectionUtils.isEmpty(gitlabGroups)) return;
            gitlabGroups.parallelStream().forEach(e -> saveGitlabGroup(csGitlabInstance, e, groupMap));
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
        if (csApplication.getEnableGitflow() && !IDUtil.isEmpty(scmMemberBranchQuery.getCiJobId())) {
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
                    gitlabBranchHandler.createBranch(csGitlabInstance.getName(), csGitlabProject.getProjectId(), branch, DEFAULT_REF_BRANCH);
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

    @Override
    public BusinessWrapper<Boolean> addGitlabGroupMember(GitlabGroupParam.AddMember addMember) {
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(addMember.getInstanceId());
        if (csGitlabInstance == null)
            return new BusinessWrapper<>(ErrorEnum.GITLAB_INSTANCE_NOT_EXIST);
        try {
            GitlabAccessLevel accessLevel = GitlabAccessLevel.fromAccessValue(addMember.getAccessLevel());
            GitlabUser user = queryUser(csGitlabInstance.getName(), addMember.getUsername());
            if (user == null)
                return new BusinessWrapper<>(ErrorEnum.GITLAB_USER_NOT_EXIST);
            if (isGroupMember(csGitlabInstance.getName(), addMember.getGroupId(), user)) {
                gitlabUserHandler.deleteGroupMember(csGitlabInstance.getName(), user.getId(), addMember.getGroupId());
            }
            gitlabGroupHandler.addGroupMember(csGitlabInstance.getName(), addMember.getGroupId(), user.getId(), accessLevel);
        } catch (IllegalArgumentException e) {
            return new BusinessWrapper<>(700001, e.getMessage());
        } catch (IOException e) {
            return new BusinessWrapper<>(ErrorEnum.GITLAB_API_ERROR);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public boolean isGroupMember(String gitlabName, Integer groupId, GitlabUser user) {
        List<GitlabGroupMember> members = gitlabGroupHandler.getGroupMembers(gitlabName, groupId);
        if (CollectionUtils.isEmpty(members)) return false;
        return members.stream().anyMatch(e -> e.getId().equals(user.getId()));
    }

    @Override
    public GitlabUser queryUser(String gitlabName, String usernmae) {
        try {
            List<GitlabUser> users = gitlabUserHandler.queryUsers(gitlabName, usernmae);
            for (GitlabUser user : users) {
                if (user.getUsername().equals(usernmae))
                    return user;
            }
        } catch (IOException ignored) {
        }
        return null;
    }


    private BusinessWrapper<GitlabBranchVO.Repository> queryGitlabProjectRepository(int id, boolean enableTag, boolean enableGitflow, String envName) {
        CsGitlabProject csGitlabProject = csGitlabProjectService.queryCsGitlabProjectById(id);
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(csGitlabProject.getInstanceId());
        List<GitlabBranchVO.BaseBranch> branches = GitlabBranchConvert.convertBranches(gitlabBranchHandler.getBranches(csGitlabInstance.getName(), csGitlabProject.getProjectId()));
        if (enableGitflow && !StringUtils.isEmpty(envName))
            branches = branches.stream().filter(e ->
                    GitlabUtil.filterBranchByGitflow(envName, e)).collect(Collectors.toList());
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

    @Override
    public DataTable<GitlabEventVO.Event> queryGitlabEventPage(GitlabEventParam.GitlabEventPageQuery pageQuery) {
        DataTable<CsGitlabWebhook> table = gitlabWebhookService.queryCsGitlabWebhookByParam(pageQuery);
        return gitalbEventWarp.wrap(table, pageQuery);
    }

}