package com.baiyi.caesar.common.util;

import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/10/22 4:57 下午
 * @Version 1.0
 */
public class GitlabUtils {

    // refs/heads/feature/deliver-report

    private static final String REFS_BRANCH_PREFIX = "refs/heads/";

    public static String getBranch(String ref) {
        if (StringUtils.isEmpty(ref))
            return null;
        if (!ref.startsWith(REFS_BRANCH_PREFIX))
            return null;
        return ref.replace(REFS_BRANCH_PREFIX, "");
    }
}
