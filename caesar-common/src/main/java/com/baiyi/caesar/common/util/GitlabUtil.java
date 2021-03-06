package com.baiyi.caesar.common.util;

import com.baiyi.caesar.domain.vo.gitlab.GitlabBranchVO;
import com.google.common.base.Joiner;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/10/22 4:57 下午
 * @Version 1.0
 */
public class GitlabUtil {

    private GitlabUtil(){
    }

    // refs/heads/feature/deliver-report
    public static final String COMMIT_BASE = "-/commit";

    private static final String REFS_BRANCH_PREFIX = "refs/heads/";

    public static String getBranch(String ref) {
        if (StringUtils.isEmpty(ref))
            return null;
        if (!ref.startsWith(REFS_BRANCH_PREFIX))
            return null;
        return ref.replace(REFS_BRANCH_PREFIX, "");
    }

    public static boolean filterBranchByGitflow(String envName, GitlabBranchVO.BaseBranch baseBranch) {
        if ("dev".equals(envName) || "daily".equals(envName)) {
            return baseBranch.getName().equals("dev")
                    || baseBranch.getName().equals("develop")
                    || baseBranch.getName().equals("daily")
                    || baseBranch.getName().startsWith("feature/")
                    || baseBranch.getName().startsWith("support/")
                    || baseBranch.getName().startsWith("release/")
                    || baseBranch.getName().startsWith("hotfix/")
                    || baseBranch.getName().equals("master");
        }
        if ("gray".equals(envName)) {
            return baseBranch.getName().equals("gray")
                    || baseBranch.getName().startsWith("support/")
                    || baseBranch.getName().startsWith("release/")
                    || baseBranch.getName().startsWith("hotfix/")
                    || baseBranch.getName().equals("master");
        }
        if ("prod".equals(envName)) {
            return baseBranch.getName().startsWith("support/")
                    || baseBranch.getName().startsWith("hotfix/")
                    || baseBranch.getName().equals("master");
        }
        return true;
    }

    /**
     * 取CommitUrl
     * @param webUrl
     * @param commitId
     * @return
     */
    public static String buildCommitUrl(String webUrl, String commitId) {
        return Joiner.on("/").join(webUrl, COMMIT_BASE, commitId);
    }
}
