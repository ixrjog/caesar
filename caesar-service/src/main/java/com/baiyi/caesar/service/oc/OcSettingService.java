package com.baiyi.caesar.service.oc;

import com.baiyi.caesar.domain.generator.caesar.OcSetting;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/4 4:18 下午
 * @Version 1.0
 */
public interface OcSettingService {

    OcSetting queryOcSettingByName(String name);

    List<OcSetting> queryAll();

    void updateOcSetting(OcSetting ocSetting);
}
