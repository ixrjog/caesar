package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.generator.caesar.OcUserSetting;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/17 1:00 下午
 * @Version 1.0
 */
public interface OcUserSettingService {

    void addOcUserSetting(OcUserSetting ocUserSetting);

    void updateOcUserSetting(OcUserSetting ocUserSetting);

    List<OcUserSetting> queryOcUserSettingBySettingGroup(int userId, String settingGroup);
}
