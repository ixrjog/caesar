package com.baiyi.caesar.aliyun.core.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.caesar.aliyun.core.AliyunCore;
import com.baiyi.caesar.aliyun.core.config.AliyunCoreConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 3:49 下午
 * @Since 1.0
 */
@Component
public class AliyunCoreImpl implements AliyunCore {

    @Resource
    private AliyunCoreConfig aliyunCoreConfig;

    @Override
    public AliyunCoreConfig.Account getAccount() {
        return aliyunCoreConfig.getAccount();
    }

    @Override
    public IAcsClient getAcsClient(String regionId, AliyunCoreConfig.Account account) {
        String defRegionId;
        if (StringUtils.isEmpty(account.getRegionId())) {
            defRegionId = account.getRegionId();
        } else {
            defRegionId = regionId;
        }
        IClientProfile profile = DefaultProfile.getProfile(defRegionId, account.getAccessKeyId(), account.getSecret());
        return new DefaultAcsClient(profile);
    }

    @Override
    public IAcsClient getAcsClient(String regionId) {
        AliyunCoreConfig.Account account = getAccount();
        return getAcsClient(regionId, account);
    }

    @Override
    public IAcsClient getMasterClient() {
        AliyunCoreConfig.Account master = getAccount();
        return getAcsClient(master.getRegionId());
    }
}
