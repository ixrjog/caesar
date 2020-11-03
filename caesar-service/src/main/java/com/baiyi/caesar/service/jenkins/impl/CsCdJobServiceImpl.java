package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.param.application.CdJobParam;
import com.baiyi.caesar.mapper.caesar.CsCdJobMapper;
import com.baiyi.caesar.service.jenkins.CsCdJobService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/26 2:45 下午
 * @Version 1.0
 */
@Service
public class CsCdJobServiceImpl implements CsCdJobService {

    @Resource
    private CsCdJobMapper csCdJobMapper;

    @Override
    public DataTable<CsCdJob> queryCsCdJobByParam(CdJobParam.CdJobPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsCdJob> list = csCdJobMapper.queryCsCdJobByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public DataTable<CsCdJob> queryCsCdJobByParam(CdJobParam.CdJobTplPageQuery pageQuery) {
        Example example = new Example(CsCdJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobTplId", pageQuery.getJobTplId());
        criteria.andLike("name", "%" + pageQuery.getQueryName() + "%").orLike("comment", "%" + pageQuery.getQueryName() + "%");
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsCdJob> list = csCdJobMapper.selectByExample(example);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addCsCdJob(CsCdJob csCdJob) {
        csCdJobMapper.insert(csCdJob);
    }

    @Override
    public void updateCsCdJob(CsCdJob csCdJob) {
        csCdJobMapper.updateByPrimaryKey(csCdJob);
    }

    @Override
    public void deleteCsCdJobById(int id) {
        csCdJobMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CsCdJob queryCsCdJobByUniqueKey(int applicationId, String jobKey) {
        Example example = new Example(CsCdJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        criteria.andEqualTo("jobKey", jobKey);
        return csCdJobMapper.selectOneByExample(example);
    }

    @Override
    public CsCdJob queryCsCdJobById(int id) {
        return csCdJobMapper.selectByPrimaryKey(id);
    }
}
