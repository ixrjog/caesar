package com.baiyi.caesar.dingtalk.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/7/28 1:35 下午
 * @Version 1.0
 */
@Data
@Builder
public class Text {

    @Builder.Default
    private String content = "CAESAR测试: 部署消息";
}
