package com.baiyi.caesar.dingtalk.impl;

import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.common.base.NoticeType;
import com.baiyi.caesar.common.config.HostConfig;
import com.baiyi.caesar.common.util.BeetlUtil;
import com.baiyi.caesar.dingtalk.DingtalkNotifyFactory;
import com.baiyi.caesar.dingtalk.IDingtalkNotify;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateBuilder;
import com.baiyi.caesar.dingtalk.builder.DingtalkTemplateMap;
import com.baiyi.caesar.dingtalk.config.DingtalkConfig;
import com.baiyi.caesar.dingtalk.content.DingtalkContent;
import com.baiyi.caesar.dingtalk.handler.DingtalkHandler;
import com.baiyi.caesar.dingtalk.util.AtUserUtils;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.base.BuildType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.param.user.UserParam;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import com.baiyi.caesar.service.dingtalk.CsDingtalkTemplateService;
import com.baiyi.caesar.service.env.OcEnvService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsJobBuildChangeService;
import com.baiyi.caesar.service.jenkins.CsJobBuildServerService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:13 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseDingtalkNotify implements IDingtalkNotify, InitializingBean {

    @Resource
    protected HostConfig hostConfig;

    @Resource
    protected CsDingtalkTemplateService csDingtalkTemplateService;

    @Resource
    private CsJobBuildChangeService csJobBuildChangeService;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private DingtalkHandler dingtalkHandler;

    @Resource
    private DingtalkConfig dingtalkConfig;

    @Resource
    private CsDingtalkService csDingtalkService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private CsJobBuildServerService csJobBuildServerService;

    abstract protected int getBuildType();

    @Override
    public void doNotify(int noticePhase, BuildJobContext context) {
        int noticeType = NoticeType.BUILD.getType();
        CsDingtalkTemplate csDingtalkTemplate = acqDingtalkTemplateByNoticeType(noticeType, noticePhase);
        if (csDingtalkTemplate == null) {
            log.error("钉钉通知模版未配置, buildId = {}", context.getJobBuild().getId());
            return;
        }
        // 模版变量
        Map<String, Object> contentMap = buildTemplateContent(noticePhase, context);
        beforeEvent(context, contentMap);
        try {
            CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(context.getCsCiJob().getDingtalkId());
            DingtalkContent dingtalkContent = DingtalkContent.builder()
                    .msg(renderTemplate(csDingtalkTemplate, contentMap))
                    .webHook(buildWebHook(csDingtalk))
                    .build();
            dingtalkHandler.doNotify(dingtalkContent);
        } catch (IOException ignored) {
        }
    }

    protected void beforeEvent(BuildJobContext context, Map<String, Object> contentMap) {

    }

    private String buildWebHook(CsDingtalk csDingtalk) {
        return dingtalkConfig.buildWebHook(decryptToken(csDingtalk.getDingtalkToken()));
    }

    /**
     * 解密
     *
     * @param ciphertext
     * @return
     */
    private String decryptToken(String ciphertext) {
        return stringEncryptor.decrypt(ciphertext);
    }

    @Override
    public void doNotify(int noticePhase, DeploymentJobContext context) {
        int noticeType = NoticeType.DEPLOYMENT.getType();
        CsDingtalkTemplate csDingtalkTemplate = acqDingtalkTemplateByNoticeType(noticeType, noticePhase);
        if (csDingtalkTemplate == null) {
            log.error("钉钉通知模版未配置, buildId = {}", context.getJobBuild().getId());
            return;
        }
        // 模版变量
        Map<String, Object> contentMap = buildTemplateContent(noticePhase, context);
        try {
            CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(context.getCsCiJob().getDingtalkId());
            DingtalkContent dingtalkContent = DingtalkContent.builder()
                    .msg(renderTemplate(csDingtalkTemplate, contentMap))
                    .webHook(buildWebHook(csDingtalk))
                    .build();
            dingtalkHandler.doNotify(dingtalkContent);
        } catch (IOException ignored) {
        }
    }

    /**
     * 部署任务
     * 取模版内容
     *
     * @param context
     * @return
     */
    protected Map<String, Object> buildTemplateContent(int noticePhase, DeploymentJobContext context) {

        OcEnv ocEnv = ocEnvService.queryOcEnvByType(context.getCsCiJob().getEnvType());
        OcUser ocUser = ocUserService.queryOcUserByUsername(context.getJobBuild().getUsername());

        DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                .paramEntryByApplicationName(context.getCsApplication().getApplicationKey())
                .paramEntryByJobName(context.getCsCiJob().getJobKey())
                .paramEntryByBuildPhase(noticePhase == NoticePhase.START.getType() ? "构建开始" : "构建结束")
                .paramEntryByEnvName(ocEnv)
                .paramEntryByDisplayName(context.getJobBuild().getUsername(), ocUser)
                .paramEntryByConsoleUrl(context.getJobBuild().getJobBuildUrl())
                .paramEntryByBuildNumber(context.getJobBuild().getJobBuildNumber())
                .paramEntryByUsers(acqAtUsers(ocUser, context.getCsApplication().getId(), context.getCsCiJob()))
                .paramEntryByBuildStatus(noticePhase == NoticePhase.END.getType() ? context.getJobBuild().getBuildStatus() : null)
                .build();
        return templateMap.getTemplate();
    }

    /**
     * 构建任务
     * 取模版内容
     *
     * @param context
     * @return
     */
    protected Map<String, Object> buildTemplateContent(int noticePhase, BuildJobContext context) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(context.getCsCiJob().getEnvType());
        OcUser ocUser = ocUserService.queryOcUserByUsername(context.getJobBuild().getUsername());

        DingtalkTemplateMap templateMap = DingtalkTemplateBuilder.newBuilder()
                .paramEntryByApplicationName(context.getCsApplication().getApplicationKey())
                .paramEntryByJobName(context.getCsCiJob().getJobKey())
                .paramEntryByBuildPhase(noticePhase == NoticePhase.START.getType() ? "构建开始" : "构建结束")
                .paramEntryByEnvName(ocEnv)
                .paramEntryByDisplayName(context.getJobBuild().getUsername(), ocUser)
                .paramEntryByConsoleUrl(context.getJobBuild().getJobBuildUrl())
                .paramEntryByBuildNumber(context.getJobBuild().getJobBuildNumber())
                .paramEntryByBranch(context.getJobBuild().getBranch())
                .paramEntryByCommit(context.getJobBuild().getCommit(), 8)
                .paramEntryByCommitUrl(Joiner.on("/").join(context.getCsGitlabProject().getWebUrl(), "-", "commit", context.getJobBuild().getCommit()))
                .paramEntryByUsers(acqAtUsers(ocUser, context.getCsApplication().getId(), context.getCsCiJob()))
                .paramEntryByChanges(noticePhase == NoticePhase.END.getType() ? acqChanges(BuildType.BUILD.getType(), context.getJobBuild().getId()) : null)
                .paramEntryByBuildStatus(noticePhase == NoticePhase.END.getType() ? context.getJobBuild().getBuildStatus() : null)
                .paramEntryByHref(context.getCsCiJob().getHref())
                .build();
        return templateMap.getTemplate();
    }

    private List<CsJobBuildChange> acqChanges(int buildType, int buildId) {
        List<CsJobBuildChange> changes = csJobBuildChangeService.queryCsJobBuildChangeByBuildId(buildType, buildId);
        if (CollectionUtils.isEmpty(changes)) return changes;
        return changes.stream().peek(e -> {
            String msg = e.getCommitMsg().replaceAll("(\n|\r\n|\"|'|\\+|-)", "");
            e.setCommitMsg(msg);
        }).collect(Collectors.toList());
    }

    /**
     * 取通知用户
     *
     * @param ocUser
     * @param applicationId
     * @param csCiJob
     * @return
     */
    private List<OcUser> acqAtUsers(OcUser ocUser, int applicationId, CsCiJob csCiJob) {
        if (csCiJob.getAtAll()) {
            UserParam.UserIncludeApplicationPageQuery pageQuery = new UserParam.UserIncludeApplicationPageQuery();
            pageQuery.setApplicationId(applicationId);
            pageQuery.setPage(1);
            pageQuery.setLength(20);
            DataTable<OcUser> dataTable = ocUserService.queryApplicationIncludeUserParam(pageQuery);
            return AtUserUtils.acqAtUsers(ocUser, dataTable.getData());
        } else {
            return Lists.newArrayList(ocUser);
        }
    }

    protected List<CsJobBuildServer> acqBuildServers(int buildId) {
        return csJobBuildServerService.queryCsJobBuildServerByBuildId(getBuildType(), buildId);
    }

    /**
     * 渲染模版
     *
     * @param csDingtalkTemplate
     * @param contentMap
     * @return
     * @throws IOException
     */
    private String renderTemplate(CsDingtalkTemplate csDingtalkTemplate, Map<String, Object> contentMap) throws IOException {
        return BeetlUtil.renderTemplate(csDingtalkTemplate.getNoticeTemplate(), contentMap);
    }

    private CsDingtalkTemplate acqDingtalkTemplateByNoticeType(int noticeType, int noticePhase) {
        return csDingtalkTemplateService.queryCsDingtalkTemplateByUniqueKey(getKey(), noticeType, noticePhase);
    }

    protected CsCiJobBuild acqCiJobBuild(int ciBuildId) {
        return csCiJobBuildService.queryCiJobBuildById(ciBuildId);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        DingtalkNotifyFactory.register(this);
    }
}
