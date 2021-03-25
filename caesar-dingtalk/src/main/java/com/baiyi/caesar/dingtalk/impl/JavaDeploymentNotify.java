package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateBuilder;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateMap;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/9/11 2:38 下午
 * @Version 1.0
 */
@Slf4j
@Component("JavaDeploymentNotify")
public class JavaDeploymentNotify extends BaseDingtalkNotify implements IDingtalkNotify {

    public static final String SERVER_GROUP = "serverGroup"; // 服务器组
    public static final String HOST_PATTERN = "hostPattern"; // 主机分组
    public static final String SERVERS = "servers"; // 主机分组

    @Override
    public String getKey() {
        return JobType.JAVA_DEPLOYMENT.getType();
    }

    @Override
    protected int getBuildType() {
        return BuildType.DEPLOYMENT.getType();
    }

    @Override
    protected Map<String, Object> buildTemplateContent(int noticePhase, DeploymentJobContext context) {
        CsCiJobBuild csCiJobBuild = acqCiJobBuild(context.getJobBuild().getCiBuildId());

        DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                .paramEntries(super.buildTemplateContent(noticePhase, context))
                .paramEntryByBuildPhase(noticePhase == NoticePhase.START.getType() ? "发布开始" : "发布结束")
                .paramEntryByServerGroup(context.getJobParamDetail().getParamByKey(SERVER_GROUP))
                .paramEntryByHostPattern(context.getJobParamDetail().getParamByKey(HOST_PATTERN))
                .paramEntryByServers(acqBuildServers(context.getJobBuild().getId()))
                .paramEntryByVersionName(csCiJobBuild.getVersionName())
                .paramEntryByBuildDetailsUrl(noticePhase == NoticePhase.END.getType() ? acqBuildDetailsUrl(context.getJobBuild().getId()) : null)
                .build();

        return templateMap.getTemplate();
    }


    // https://caesar.example.org/index.html#/job/build/android/reinforce?buildId=168
    private String acqBuildDetailsUrl(int buildId) {
        return Joiner.on("/").join(hostConfig.getUrl(), "index.html#/job/build/android/reinforce?buildId=") + buildId;
    }

}