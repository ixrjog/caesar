package com.baiyi.caesar.service.aliyun.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import com.baiyi.caesar.domain.param.aliyun.OSSBucketParam;
import com.baiyi.caesar.mapper.caesar.CsOssBucketMapper;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:16 下午
 * @Version 1.0
 */
@Service
public class CsOssBucketServiceImpl implements CsOssBucketService {

    @Resource
    private CsOssBucketMapper csOssBucketMapper;

    @Override
    public DataTable<CsOssBucket> queryCsOssBucketByParam(OSSBucketParam.BucketPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsOssBucket> data = csOssBucketMapper.queryCsOssBucketByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public CsOssBucket queryCsOssBucketByName(String name) {
        Example example = new Example(CsOssBucket.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        PageHelper.startPage(1, 1);
        return csOssBucketMapper.selectOneByExample(example);
    }

    @Override
    public CsOssBucket queryCsOssBucketById(int id) {
        return csOssBucketMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsOssBucket(CsOssBucket csOssBucket) {
        csOssBucketMapper.insert(csOssBucket);
    }

    @Override
    public void updateCsOssBucket(CsOssBucket csOssBucket) {
        csOssBucketMapper.updateByPrimaryKey(csOssBucket);
    }

    @Override
    public void deleteCsOssBucketById(int id) {
        csOssBucketMapper.deleteByPrimaryKey(id);
    }

}
