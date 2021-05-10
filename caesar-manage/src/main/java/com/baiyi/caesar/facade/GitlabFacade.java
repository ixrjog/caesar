package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabGroup;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabEventParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import com.baiyi.caesar.domain.vo.gitlab.*;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:42 上午
 * @Version 1.0
 */
public interface GitlabFacade {

    DataTable<GitlabInstanceVO.Instance> queryGitlabInstancePage(GitlabInstanceParam.PageQuery pageQuery);

    void webhooksV1(GitlabHookVO.Webhook webhook);

    void systemHooksV1(GitlabHookVO.SystemHook systemHook);

    BusinessWrapper<Boolean> addGitlabInstance(GitlabInstanceVO.Instance instance);

    BusinessWrapper<Boolean> updateGitlabInstance(GitlabInstanceVO.Instance instance);

    BusinessWrapper<Boolean> deleteGitlabInstanceById(int id);

    void syncGitlabInstanceProject(int instanceId);

    void syncGitlabInstanceGroup(int instanceId);

    void saveGitlabProject(CsGitlabInstance csGitlabInstance, GitlabProject gitlabProject, Map<Integer, CsGitlabProject> projectMap);

    void saveGitlabGroup(CsGitlabInstance csGitlabInstance, GitlabGroup gitlabGroup, Map<Integer, CsGitlabGroup> groupMap);

    DataTable<GitlabProjectVO.Project> queryGitlabProjectPage(GitlabProjectParam.GitlabProjectPageQuery pageQuery);

    DataTable<GitlabGroupVO.Group> queryGitlabGroupPage(GitlabGroupParam.GitlabGroupPageQuery pageQuery);

    BusinessWrapper<GitlabBranchVO.Repository> queryApplicationSCMMemberBranch(ApplicationParam.ScmMemberBranchQuery scmMemberBranchQuery);

    BusinessWrapper<Boolean> createApplicationSCMMemberBranch(ApplicationParam.CreateScmMemberBranch createParam);

    BusinessWrapper<GitlabBranchVO.BaseBranch> queryApplicationSCMMemberBranchCommit(ApplicationParam.ScmMemberBranchCommitQuery query);

    BusinessWrapper<Boolean> addGitlabGroupMember(GitlabGroupParam.AddMember addMember);

    GitlabUser queryUser(String gitlabName, String usernmae);

    boolean isGroupMember(String gitlabName, Integer groupId, GitlabUser user);

    DataTable<GitlabEventVO.Event> queryGitlabEventPage(GitlabEventParam.GitlabEventPageQuery pageQuery);

}
