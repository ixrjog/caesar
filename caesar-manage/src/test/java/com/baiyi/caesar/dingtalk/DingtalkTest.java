package com.baiyi.caesar.dingtalk;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.dingtalk.config.DingtalkConfig;
import com.baiyi.caesar.dingtalk.content.DingtalkContent;
import com.baiyi.caesar.dingtalk.handler.DingtalkHandler;
import com.baiyi.caesar.dingtalk.model.TestMessage;
import com.baiyi.caesar.facade.DingtalkFacade;
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
    void testDingtalkNotify() {
        IDingtalkNotify dingtalkNotify =  DingtalkNotifyFactory.getDingtalkNotifyByKey(JobType.HTML5.getType());
       // dingtalkNotify.doNotify(0,0,null);
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
