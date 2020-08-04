package com.baiyi.caesar.aliyun.core.client;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.caesar.aliyun.core.config.AliyunCoreConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/31 2:17 下午
 * @Version 1.0
 */
@Component
public class AliyunClient {

    @Resource
    private AliyunCoreConfig aliyunCoreConfig;

    public IAcsClient getAcsClient(String regionId) {
        AliyunCoreConfig.Account account = aliyunCoreConfig.getAccount();
        String defRegionId;
        if (StringUtils.isEmpty(account.getRegionId())) {
            defRegionId = account.getRegionId();
        } else {
            defRegionId = regionId;
        }
        IClientProfile profile = DefaultProfile.getProfile(defRegionId, account.getAccessKeyId(), account.getSecret());
        return new DefaultAcsClient(profile);
    }
}
