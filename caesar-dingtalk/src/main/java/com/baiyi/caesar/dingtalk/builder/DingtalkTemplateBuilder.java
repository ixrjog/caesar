package com.baiyi.caesar.dingtalk.builder;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildChange;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildServer;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

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

    public static final String COMMIT_URL = "commit_url";

    public static final String USERS = "users";
    public static final String BUILD_STATUS = "buildStatus";

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

    public DingtalkTemplateBuilder paramEntryServers(List<CsJobBuildServer> servers) {
        templateMap.putContent(SERVERS, servers);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryHostPattern(String hostPattern) {
        templateMap.putContent(HOST_PATTERN, hostPattern);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryServerGroup(String serverGroup) {
        templateMap.putContent(SERVER_GROUP, serverGroup);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryBuildDetailsUrl(String buildDetailsUrl) {
        if (!StringUtils.isEmpty(buildDetailsUrl))
            templateMap.putContent(BUILD_DETAILS_URL, buildDetailsUrl);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryProductFlavor(String productFlavor) {
        templateMap.putContent(PRODUCT_FLAVOR, productFlavor);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryBuildType(String buildType) {
        templateMap.putContent(BUILD_TYPE, buildType);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryVersionName(String versionName) {
        if (!StringUtils.isEmpty(versionName))
            templateMap.putContent(VERSION_NAME, versionName);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryChanges(List<CsJobBuildChange> changes) {
        templateMap.putContent(CHANGES, changes);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryBuildStatus(String buildStatus) {
        if (!StringUtils.isEmpty(buildStatus))
            templateMap.putContent(BUILD_STATUS, buildStatus);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryCommit(String commit, int length) {
        if (StringUtils.isEmpty(commit)) {
            templateMap.putContent(COMMIT, "-");
        } else {
            templateMap.putContent(COMMIT, commit.substring(0, length));
        }
        return this;
    }

    public DingtalkTemplateBuilder paramEntryCommitUrl(String commitUrl) {
        if (!StringUtils.isEmpty(commitUrl))
            templateMap.putContent(COMMIT_URL, commitUrl);
        return this;
    }


    public DingtalkTemplateBuilder paramEntryBranch(String branch) {
        templateMap.putContent(BRANCH, branch);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryUsers(List<OcUser> users) {
        templateMap.putContent(USERS, users);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryBuildNumber(Integer buildNumber) {
        templateMap.putContent(BUILD_NUMBER, buildNumber);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryConsoleUrl(String jobBuildUrl) {
        templateMap.putContent(CONSOLE_URL, Joiner.on("/").join(jobBuildUrl, "console"));
        return this;
    }

    public DingtalkTemplateBuilder paramEntryDisplayName(String username, OcUser ocUser) {
        templateMap.putContent(DISPLAY_NAME, ocUser != null ? ocUser.getDisplayName() : username);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryApplicationName(String applicationName) {
        templateMap.putContent(APPLICATION_NAME, applicationName);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryJobName(String jobName) {
        templateMap.putContent(JOB_NAME, jobName);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryBuildPhase(String noticePhase) {
        templateMap.putContent(BUILD_PHASE, noticePhase);
        return this;
    }

    public DingtalkTemplateBuilder paramEntryEnvName(OcEnv ocEnv) {
        templateMap.putContent(ENV_NAME, ocEnv != null ? ocEnv.getEnvName() : "default");
        return this;
    }

    public DingtalkTemplateBuilder paramEntry(String paramName, String value) {
        if (!StringUtils.isEmpty(value))
            templateMap.putContent(paramName, value);
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
