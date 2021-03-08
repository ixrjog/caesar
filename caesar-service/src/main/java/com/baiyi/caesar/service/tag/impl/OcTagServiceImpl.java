package com.baiyi.caesar.service.tag.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcTag;
import com.baiyi.caesar.domain.param.tag.TagParam;
import com.baiyi.caesar.mapper.caesar.OcTagMapper;
import com.baiyi.caesar.service.tag.OcTagService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/22 1:04 下午
 * @Version 1.0
 */
@Service
public class OcTagServiceImpl implements OcTagService {

    @Resource
    private OcTagMapper ocTagMapper;

    @Override
    public OcTag queryOcTagById(Integer id) {
        return ocTagMapper.selectByPrimaryKey(id);
    }

    @Override
    public OcTag queryOcTagByKey(String tagKey) {
        Example example = new Example(OcTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tagKey", tagKey);
        return ocTagMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<OcTag> queryOcTagByParam(TagParam.TagPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcTag> ocTagList = ocTagMapper.queryOcTagByParam(pageQuery);
        return new DataTable<>(ocTagList, page.getTotal());
    }

    @Override
    public void addOcTag(OcTag ocTag) {
        ocTagMapper.insert(ocTag);
    }

    @Override
    public void updateOcTag(OcTag ocTag) {
        ocTagMapper.updateByPrimaryKey(ocTag);
    }

    @Override
    public void deleteOcTagById(int id) {
        ocTagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcTag> queryOcTagByParam(TagParam.BusinessQuery businessQuery) {
        return ocTagMapper.queryOcTagByBusinessParam(businessQuery);
    }

    @Override
    public List<OcTag> queryOcTagNotInByParam(TagParam.BusinessQuery businessQuery){
        return ocTagMapper.queryOcTagNotInByBusinessParam(businessQuery);
    }


}
