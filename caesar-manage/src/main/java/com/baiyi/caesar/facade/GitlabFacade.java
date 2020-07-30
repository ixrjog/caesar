package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.domain.vo.gitlab.GitlabProjectVO;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:42 上午
 * @Version 1.0
 */
public interface GitlabFacade {

    DataTable<GitlabInstanceVO.Instance> queryGitlabInstancePage(GitlabInstanceParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addGitlabInstance(GitlabInstanceVO.Instance instance);

    BusinessWrapper<Boolean> updateGitlabInstance(GitlabInstanceVO.Instance instance);

    BusinessWrapper<Boolean> deleteGitlabInstanceById(int id);

    void syncGitlabInstanceProject(int instanceId);

    DataTable<GitlabProjectVO.Project> queryGitlabProjectPage(GitlabProjectParam.PageQuery pageQuery);

    BusinessWrapper<GitlabBranchVO.Repository> queryGitlabProjectRepository(int id, boolean enableTag);
}
