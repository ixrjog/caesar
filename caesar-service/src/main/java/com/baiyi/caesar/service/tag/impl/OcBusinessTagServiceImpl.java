package com.baiyi.caesar.service.tag.impl;

import com.baiyi.caesar.domain.generator.caesar.OcBusinessTag;
import com.baiyi.caesar.domain.vo.tag.BusinessTagVO;
import com.baiyi.caesar.mapper.caesar.OcBusinessTagMapper;
import com.baiyi.caesar.service.tag.OcBusinessTagService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:08 下午
 * @Version 1.0
 */
@Service
public class OcBusinessTagServiceImpl implements OcBusinessTagService {

    @Resource
    private OcBusinessTagMapper ocBusinessTagMapper;

    @Override
    public OcBusinessTag queryOcBusinessTagByUniqueKey(BusinessTagVO.BusinessTag businessTag) {
        Example example = new Example(OcBusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessId", businessTag.getBusinessId());
        criteria.andEqualTo("businessType",businessTag.getBusinessType());
        criteria.andEqualTo("tagId",businessTag.getTagId());
        return ocBusinessTagMapper.selectOneByExample(example);
    }

    @Override
    public List<OcBusinessTag> queryOcBusinessTagByBusinessTypeAndBusinessId(int businessType, int businessId) {
        Example example = new Example(OcBusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessId", businessId);
        criteria.andEqualTo("businessType", businessType);
        return ocBusinessTagMapper.selectByExample(example);
    }

    @Override
    public void deleteOcBusinessTagByUniqueKey(BusinessTagVO.BusinessTag businessTag) {
        ocBusinessTagMapper.deleteOcBusinessTagByUniqueKey(businessTag);
    }

    @Override
    public void addOcBusinessTag(OcBusinessTag ocBusinessTag) {
        ocBusinessTagMapper.insert(ocBusinessTag);
    }

    @Override
    public void deleteOcBusinessTagById(int id) {
        ocBusinessTagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int countOcTagHasUsed(int tagId) {
        Example example = new Example(OcBusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tagId", tagId);
        return ocBusinessTagMapper.selectCountByExample(example);
    }

}
