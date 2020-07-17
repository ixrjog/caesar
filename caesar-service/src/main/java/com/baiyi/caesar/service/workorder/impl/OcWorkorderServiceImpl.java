package com.baiyi.caesar.service.workorder.impl;

import com.baiyi.caesar.domain.generator.caesar.OcWorkorder;
import com.baiyi.caesar.mapper.caesar.OcWorkorderMapper;
import com.baiyi.caesar.service.workorder.OcWorkorderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author baiyi
 * @Date 2020/4/28 10:29 上午
 * @Version 1.0
 */
@Service
public class OcWorkorderServiceImpl implements OcWorkorderService {

    @Resource
    private OcWorkorderMapper ocWorkorderMapper;

    @Override
    public OcWorkorder queryOcWorkorderByWorkorderKey(String workorderKey) {
        Example example = new Example(OcWorkorder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workorderKey", workorderKey);
        return ocWorkorderMapper.selectOneByExample(example);
    }

    @Override
    public List<OcWorkorder> queryOcWorkorderByGroupId(int workorderGroupId, boolean isDevelopment) {
        Example example = new Example(OcWorkorder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workorderGroupId",workorderGroupId);
        if(!isDevelopment)
            criteria.andEqualTo("workorderStatus",0);
        return ocWorkorderMapper.selectByExample(example);
    }


    @Override
    public OcWorkorder queryOcWorkorderById(int id) {
        return ocWorkorderMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcWorkorder(OcWorkorder ocWorkorder) {
        ocWorkorderMapper.insert(ocWorkorder);
    }

    @Override
    public void updateOcWorkorder(OcWorkorder ocWorkorder) {
        ocWorkorderMapper.updateByPrimaryKey(ocWorkorder);
    }


}
