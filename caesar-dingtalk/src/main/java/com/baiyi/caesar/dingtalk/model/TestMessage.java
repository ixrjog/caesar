package com.baiyi.caesar.dingtalk.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/7/28 11:30 上午
 * @Version 1.0
 */
@Data
@Builder
public class TestMessage {

    @Builder.Default
    private String msgtype = "text";

    @Builder.Default
    private Text text = Text.builder().build();

}
