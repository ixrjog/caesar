package com.baiyi.caesar.dingtalk.content;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2020/7/28 10:40 上午
 * @Version 1.0
 */
@Data
@Builder
public class DingtalkContent implements Serializable {

    private static final long serialVersionUID = 2740173009928021870L;

    /**
     * 通知地址
     */
    private String webHook;

    private String msg;
}
