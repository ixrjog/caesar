package com.baiyi.caesar.gitlab;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.facade.GitlabFacade;
import com.baiyi.caesar.gitlab.handler.GitlabBranchHandler;
import com.baiyi.caesar.gitlab.handler.GitlabGroupHandler;
import com.baiyi.caesar.gitlab.handler.GitlabHandler;
import com.baiyi.caesar.gitlab.handler.GitlabProjectHandler;
import org.gitlab.api.models.*;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/20 10:25 上午
 * @Version 1.0
 */
public class GitlabTest extends BaseUnit {

    @Resource
    private GitlabHandler gitlabHandler;

    @Resource
    private GitlabProjectHandler gitlabProjectHandler;

    @Resource
    private GitlabGroupHandler gitlabGroupHandler;

    @Resource
    private GitlabFacade gitlabFacade;

    @Resource
    private GitlabBranchHandler gitlabBranchHandler;

    @Test
    void testVersion() {
        try {
            GitlabVersion gitlabVersion = gitlabHandler.getVersion("gitlab-1");
            System.err.println(gitlabVersion);
        } catch (IOException e) {
        }
    }

    @Test
    void projectTest() {
        List<GitlabProject> list = gitlabProjectHandler.getProjects("gitlab-1");
        System.err.println(list);
    }

    @Test
    void testGetProject() {
        try {
            GitlabProject p = gitlabProjectHandler.getProject("gitlab-1", 72);
            gitlabBranchHandler.createBranch("gitlab-1", 72, "test999", "master");
            System.err.println(p);
        } catch (IOException e) {
        }

    }

    @Test
    void testGetProjectBranch() {
        try {
            GitlabBranch b = gitlabBranchHandler.getBranch("gitlab-1", 72, "test998");
            System.err.println(b);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("----------------------");
            System.err.println(e.getLocalizedMessage());

        }

    }

    @Test
    void testGroup() {
        try {
            List<GitlabGroup> list = gitlabGroupHandler.getGroups("gitlab-1");
            System.err.println(list);
        } catch (IOException e) {
        }
    }

    @Test
    void testNamespace() {
        List<GitlabNamespace> list = gitlabGroupHandler.getNamespace("gitlab-1");
        System.err.println(list);
    }

    @Test
    void testSyncProject() {
        gitlabFacade.syncGitlabInstanceProject(1);
    }

    @Test
    void testGetBranches() {
        // name  commit.id/commit.message
        List<GitlabBranch> list = gitlabBranchHandler.getBranches("gitlab-1", 1661);
        System.err.println(list);
    }

    @Test
    void testGetTags() {
        List<GitlabTag> list = gitlabBranchHandler.getTags("gitlab-1", 1661);
        System.err.println(list);
    }

    @Test
    void testSyncGroup() {
        gitlabFacade.syncGitlabInstanceGroup(1);
    }

}