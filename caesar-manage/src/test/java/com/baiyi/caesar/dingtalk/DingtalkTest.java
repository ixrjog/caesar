package com.baiyi.caesar.dingtalk;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.common.base.NoticePhase;
import com.baiyi.caesar.dingtalk.config.DingtalkConfig;
import com.baiyi.caesar.dingtalk.content.DingtalkContent;
import com.baiyi.caesar.dingtalk.handler.DingtalkHandler;
import com.baiyi.caesar.dingtalk.model.TestMessage;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildChange;
import com.baiyi.caesar.facade.DingtalkFacade;
import com.baiyi.caesar.factory.jenkins.BuildJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.DeploymentJobHandlerFactory;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.factory.jenkins.IDeploymentJobHandler;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.baiyi.caesar.service.jenkins.CsJobBuildChangeService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/28 11:39 上午
 * @Version 1.0
 */
public class DingtalkTest extends BaseUnit {

    @Resource
    private DingtalkHandler dingtalkHandler;

    @Resource
    private DingtalkConfig dingtalkConfig;

    @Resource
    private DingtalkFacade dingtalkFacade;

    @Resource
    private CsCiJobBuildService csCiJobBuildService;

    @Resource
    private CsCdJobBuildService csCdJobBuildService;

    @Resource
    private CsJobBuildChangeService csJobBuildChangeService;

    @Test
    void doNotifyTest() {
        DingtalkContent content = DingtalkContent.builder()
                .msg(JSON.toJSONString(TestMessage.builder().build()))
                .webHook(dingtalkConfig.getWebHook(dingtalkFacade.acqDingtalkTokenById(1)))
                .build();
        try {
            dingtalkHandler.doNotify(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testNotifyTpl() {
        System.err.println(acqNotifyCITpl());
    }

    @Test
    void testBuildNotify() {
        String jobKey = JobType.HTML5.getType();
        IBuildJobHandler iBuildJobHandler = BuildJobHandlerFactory.getBuildJobByKey(jobKey);
        CsCiJobBuild csCiJobBuild = csCiJobBuildService.queryCiJobBuildById(3562);
        BuildJobContext context = iBuildJobHandler.acqBuildJobContext(csCiJobBuild);
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(jobKey);
        dingtalkNotify.doNotify(NoticePhase.END.getType(), context);
    }

    @Test
    void test2(){

        CsJobBuildChange csJobBuildChange = csJobBuildChangeService.queryCsJobBuildChangeById(1862);
        // String msg = csJobBuildChange.getCommitMsg().replaceAll("(\n|\r\n|\"|'|\\+|-)\\s+", "");
        String msg = csJobBuildChange.getCommitMsg().replaceAll("(\n|\r\n|\"|'|\\+|-)", "");
        System.err.println(msg);
    }

    @Test
    void testDeploymentNotify() {
        String jobKey = JobType.ANDROID_REINFORCE.getType();
        IDeploymentJobHandler iDeploymentJobHandler = DeploymentJobHandlerFactory.getDeploymentJobByKey(jobKey);
        CsCdJobBuild csCdJobBuild = csCdJobBuildService.queryCdJobBuildById(184);
        DeploymentJobContext  context = iDeploymentJobHandler.acqDeploymentJobContext(csCdJobBuild);
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(jobKey);
        dingtalkNotify.doNotify(NoticePhase.END.getType(), context);
    }

    @Test
    void testJavaDeploymentNotify() {
        String jobKey = JobType.JAVA_DEPLOYMENT.getType();
        IDeploymentJobHandler iDeploymentJobHandler = DeploymentJobHandlerFactory.getDeploymentJobByKey(jobKey);
        CsCdJobBuild csCdJobBuild = csCdJobBuildService.queryCdJobBuildById(5);
        DeploymentJobContext  context = iDeploymentJobHandler.acqDeploymentJobContext(csCdJobBuild);
        IDingtalkNotify dingtalkNotify = DingtalkNotifyFactory.getDingtalkNotifyByKey(jobKey);
        dingtalkNotify.doNotify(NoticePhase.END.getType(), context);
    }

    @Test
    void test(){
        String s = "${condition='通过'} \" \" ' ' + - ";
        String msg = s.replaceAll("(\n|\r\n|\"|'|\\+|-)\\s+", "");
        System.err.println(msg);
    }

    private String acqNotifyCITpl() {
        return "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"部署消息\"," +
                "\"text\": \"### ${projectName}\\n" +
                "${buildFinalized}" +
                "环境 : ${envName}\\n\\n" +
                "任务名称 : ${jobName}\\n\\n" +
                "任务阶段 : ${buildPhase}\\n\\n" +
                "[控制台日志](${console})\\n\\n" +
                "${buildStatus}" +
                "任务编号 : ${buildNumber}\\n\\n" +
                "分支 : ${branch}\\n\\n" +
                "COMMIT : ${commit}\\n\\n" +
                "${commitInfo}\\n\\n" +
                "操作人 : ${username}\\n\\n" +
                "${atUser}\\n\\n" +
                "${indexUrl}" +
                "${url}" +
                "\"} ${at}" +
                "}";
    }
}
