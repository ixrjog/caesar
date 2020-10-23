package com.baiyi.caesar.service.gitlab;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/20 11:03 上午
 * @Version 1.0
 */
public interface CsGitlabProjectService {

    DataTable<CsGitlabProject> queryCsGitlabProjectByParam(GitlabProjectParam.GitlabProjectPageQuery pageQuery);

    List<CsGitlabProject> queryCsGitlabProjectByInstanceId(Integer instanceId);

    int countCsGitlabProjectByInstanceId(Integer instanceId);

    CsGitlabProject queryCsGitlabProjectById(int id);

    CsGitlabProject queryCsGitlabProjectByUniqueKey(int instanceId, int projectId);

    void addCsGitlabProject(CsGitlabProject csGitlabProject);

    void updateCsGitlabProject(CsGitlabProject csGitlabProject);

    void deleteCsGitlabProjectById(int id);
}
