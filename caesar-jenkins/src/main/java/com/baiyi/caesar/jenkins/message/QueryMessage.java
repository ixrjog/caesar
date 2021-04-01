package com.baiyi.caesar.jenkins.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/3/31 3:36 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryMessage extends BaseMessage {

    private Integer buildType; // 构建类型

}
