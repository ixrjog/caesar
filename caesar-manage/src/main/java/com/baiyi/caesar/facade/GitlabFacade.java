package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabProjectVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHooks;
import org.gitlab.api.models.GitlabBranchCommit;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:42 上午
 * @Version 1.0
 */
public interface GitlabFacade {

    BusinessWrapper<GitlabBranchCommit> queryGitlabProjectBranchCommit(int id, String branch);

    DataTable<GitlabInstanceVO.Instance> queryGitlabInstancePage(GitlabInstanceParam.PageQuery pageQuery);

    void webhooksV1(GitlabHooks.Webhooks webhooks);

    BusinessWrapper<Boolean> addGitlabInstance(GitlabInstanceVO.Instance instance);

    BusinessWrapper<Boolean> updateGitlabInstance(GitlabInstanceVO.Instance instance);

    BusinessWrapper<Boolean> deleteGitlabInstanceById(int id);

    void syncGitlabInstanceProject(int instanceId);

    void syncGitlabInstanceGroup(int instanceId);

    DataTable<GitlabProjectVO.Project> queryGitlabProjectPage(GitlabProjectParam.GitlabProjectPageQuery pageQuery);

    BusinessWrapper<GitlabBranchVO.Repository> queryGitlabProjectRepository(int id, boolean enableTag);
}
