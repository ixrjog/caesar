package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.param.user.UserSettingParam;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/17 1:08 下午
 * @Version 1.0
 */
public interface UserSettingFacade {

    Map<String, String> queryUserSettingBySettingGroup(String settingGroup);

    BusinessWrapper<Boolean> saveUserSettingBySettingGroup(UserSettingParam.UserSetting userSetting);
}
