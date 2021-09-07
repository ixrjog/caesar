package com.baiyi.caesar.dingtalk.builder;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildChange;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildServer;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/1/15 10:07 上午
 * @Version 1.0
 */
public class DingtalkTemplateBuilder {

    public static final String APPLICATION_NAME = "applicationName";
    public static final String JOB_NAME = "jobName";
    public static final String BUILD_PHASE = "buildPhase";
    public static final String ENV_NAME = "envName";
    public static final String DISPLAY_NAME = "displayName";
    public static final String CONSOLE_URL = "consoleUrl";
    public static final String BUILD_NUMBER = "buildNumber";
    public static final String BRANCH = "branch";
    public static final String COMMIT = "commit";
    public static final String COMMITS = "commits";

    public static final String COMMIT_URL = "commitUrl";

    public static final String USERS = "users";
    public static final String BUILD_STATUS = "buildStatus";

    public static final String HREF = "href";

    public static final String CHANGES = "changes";

    public static final String VERSION_NAME = "versionName";

    // Android
    public static final String BUILD_TYPE = "BUILD_TYPE"; // 构建环境
    public static final String PRODUCT_FLAVOR = "PRODUCT_FLAVOR"; // 构建渠道

    public static final String BUILD_DETAILS_URL = "buildDetailsUrl";

    public static final String SERVER_GROUP = "serverGroup"; // 服务器组
    public static final String HOST_PATTERN = "hostPattern"; // 主机分组
    public static final String SERVERS = "servers"; // 主机分组


    private DingtalkTemplateMap templateMap = new DingtalkTemplateMap();

    private DingtalkTemplateBuilder() {
    }

    static public DingtalkTemplateBuilder newBuilder() {
        return new DingtalkTemplateBuilder();
    }

    public DingtalkTemplateBuilder paramEntryByServers(List<CsJobBuildServer> servers) {
        templateMap.putContent(SERVERS, servers);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByHostPattern(String hostPattern) {
        templateMap.putContent(HOST_PATTERN, hostPattern);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByServerGroup(String serverGroup) {
        templateMap.putContent(SERVER_GROUP, serverGroup);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByBuildDetailsUrl(String buildDetailsUrl) {
        if (!StringUtils.isEmpty(buildDetailsUrl))
            templateMap.putContent(BUILD_DETAILS_URL, buildDetailsUrl);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByProductFlavor(String productFlavor) {
        templateMap.putContent(PRODUCT_FLAVOR, productFlavor);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByBuildType(String buildType) {
        templateMap.putContent(BUILD_TYPE, buildType);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByVersionName(String versionName) {
        if (!StringUtils.isEmpty(versionName))
            templateMap.putContent(VERSION_NAME, versionName);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByChanges(List<CsJobBuildChange> changes) {
        templateMap.putContent(CHANGES, changes);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByBuildStatus(String buildStatus) {
        if (!StringUtils.isEmpty(buildStatus))
            templateMap.putContent(BUILD_STATUS, buildStatus);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByCommit(String commit, int length) {
        if (StringUtils.isEmpty(commit)) {
            templateMap.putContent(COMMIT, "-");
        } else {
            templateMap.putContent(COMMIT, commit.substring(0, length));
        }
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByCommitUrl(String commitUrl) {
        if (!StringUtils.isEmpty(commitUrl))
            templateMap.putContent(COMMIT_URL, commitUrl);
        return this;
    }


    public DingtalkTemplateBuilder paramEntryByBranch(String branch) {
        templateMap.putContent(BRANCH, branch);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByUsers(List<OcUser> users) {
        templateMap.putContent(USERS, users);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByBuildNumber(Integer buildNumber) {
        templateMap.putContent(BUILD_NUMBER, buildNumber);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByHref(String href) {
        templateMap.putContent(HREF, href);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByConsoleUrl(String jobBuildUrl) {
        templateMap.putContent(CONSOLE_URL, Joiner.on("/").join(jobBuildUrl, "console"));
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByDisplayName(String username, OcUser ocUser) {
        templateMap.putContent(DISPLAY_NAME, ocUser != null ? ocUser.getDisplayName() : username);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByApplicationName(String applicationName) {
        templateMap.putContent(APPLICATION_NAME, applicationName);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByJobName(String jobName) {
        templateMap.putContent(JOB_NAME, jobName);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByBuildPhase(String noticePhase) {
        templateMap.putContent(BUILD_PHASE, noticePhase);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByEnvName(OcEnv ocEnv) {
        templateMap.putContent(ENV_NAME, ocEnv != null ? ocEnv.getEnvName() : "default");
        return this;
    }

    public DingtalkTemplateBuilder paramEntry(String paramName, String value) {
        if (StringUtils.isNotEmpty(value))
            templateMap.putContent(paramName, value);
        return this;
    }

    public DingtalkTemplateBuilder paramEntry(String paramName, Object value) {
        if (ObjectUtils.isNotEmpty(value))
            templateMap.putContent(paramName, value);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryByCommits(List<GitlabHookVO.Commits> commits) {
        if (!CollectionUtils.isEmpty(commits)) {
            List<GitlabHookVO.Commits> commitsList = commits.stream().peek(x -> x.setId(x.getId().substring(0, 8))).collect(Collectors.toList());
            Collections.reverse(commitsList);
            templateMap.putContent(COMMITS, commitsList);
        }
        return this;
    }

    public DingtalkTemplateBuilder paramEntries(Map<String, Object> contents) {
        templateMap.putContents(contents);
        return this;
    }

    public DingtalkTemplateMap build() {
        return templateMap;
    }

}
