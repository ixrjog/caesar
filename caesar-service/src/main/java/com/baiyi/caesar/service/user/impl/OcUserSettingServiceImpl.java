package com.baiyi.caesar.service.user.impl;

import com.baiyi.caesar.domain.generator.caesar.OcUserSetting;
import com.baiyi.caesar.mapper.caesar.OcUserSettingMapper;
import com.baiyi.caesar.service.user.OcUserSettingService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/17 1:00 下午
 * @Version 1.0
 */
@Service
public class OcUserSettingServiceImpl implements OcUserSettingService {

    @Resource
    private OcUserSettingMapper ocUserSettingMapper;

    @Override
    public void addOcUserSetting(OcUserSetting ocUserSetting) {
        ocUserSettingMapper.insert(ocUserSetting);
    }


    @Override
    public void updateOcUserSetting(OcUserSetting ocUserSetting) {
        ocUserSettingMapper.updateByPrimaryKey(ocUserSetting);
    }

    @Override
    public List<OcUserSetting> queryOcUserSettingBySettingGroup(int userId, String settingGroup) {
        Example example = new Example(OcUserSetting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("settingGroup", settingGroup);
        return ocUserSettingMapper.selectByExample(example);
    }


}
