package com.baiyi.caesar.dingtalk;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
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
}
