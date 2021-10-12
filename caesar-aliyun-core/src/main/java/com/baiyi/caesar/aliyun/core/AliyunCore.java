package com.baiyi.caesar.aliyun.core;

import com.aliyuncs.IAcsClient;
import com.baiyi.caesar.aliyun.core.config.AliyunCoreConfig;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 3:48 下午
 * @Since 1.0
 */
public interface AliyunCore {

    AliyunCoreConfig.Account getAccount();

    IAcsClient getAcsClient(String regionId, AliyunCoreConfig.Account account);

    IAcsClient getAcsClient(String regionId);

    IAcsClient getMasterClient();

}
