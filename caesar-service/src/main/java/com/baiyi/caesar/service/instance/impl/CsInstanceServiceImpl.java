package com.baiyi.caesar.service.instance.impl;

import com.baiyi.caesar.domain.generator.caesar.CsInstance;
import com.baiyi.caesar.mapper.caesar.CsInstanceMapper;
import com.baiyi.caesar.service.instance.CsInstanceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/9/7 11:01 上午
 * @Version 1.0
 */
@Service
public class CsInstanceServiceImpl implements CsInstanceService {

    @Resource
    private CsInstanceMapper csInstanceMapper;

    @Override
    public void addCsInstance(CsInstance csInstance) {
        csInstanceMapper.insert(csInstance);
    }

    @Override
    public void updateCsInstance(CsInstance csInstance) {
        csInstanceMapper.updateByPrimaryKey(csInstance);
    }

    @Override
    public CsInstance queryCsInstanceByHostIp(String hostIp) {
        Example example = new Example(CsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("hostIp", hostIp);
        return csInstanceMapper.selectOneByExample(example);
    }

}
