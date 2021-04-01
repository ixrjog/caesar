package com.baiyi.caesar.jenkins.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/3/31 2:49 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InitialMessage extends BaseMessage {

    private String token;

}
