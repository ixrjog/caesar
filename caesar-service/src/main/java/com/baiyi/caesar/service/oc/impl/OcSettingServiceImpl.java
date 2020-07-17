package com.baiyi.caesar.service.oc.impl;

import com.baiyi.caesar.domain.generator.caesar.OcSetting;
import com.baiyi.caesar.mapper.caesar.OcSettingMapper;
import com.baiyi.caesar.service.oc.OcSettingService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/4 4:18 下午
 * @Version 1.0
 */
@Service
public class OcSettingServiceImpl implements OcSettingService {

    @Resource
    private OcSettingMapper ocSettingMapper;

    @Override
    public OcSetting queryOcSettingByName(String name) {
        Example example = new Example(OcSetting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return ocSettingMapper.selectOneByExample(example);
    }

    @Override
    public List<OcSetting> queryAll() {
        return ocSettingMapper.selectAll();
    }

    @Override
    public void updateOcSetting(OcSetting ocSetting) {
        ocSettingMapper.updateByPrimaryKey(ocSetting);
    }
}
