package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.mapper.caesar.CsCiJobMapper;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/29 11:40 上午
 * @Version 1.0
 */
@Service
public class CsCiJobServiceImpl implements CsCiJobService {

    @Resource
    private CsCiJobMapper csCiJobMapper;

    @Override
    public DataTable<CsCiJob> queryCsCiJobByParam(CiJobParam.CiJobPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsCiJob> list = csCiJobMapper.queryCsCiJobByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public DataTable<CsCiJob> queryCsCiJobByParam(CiJobParam.CiJobTplPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsCiJob> list = csCiJobMapper.queryCsCiJobTplByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addCsCiJob(CsCiJob csCiJob) {
        csCiJobMapper.insert(csCiJob);
    }

    @Override
    public void updateCsCiJob(CsCiJob csCiJob) {
        csCiJobMapper.updateByPrimaryKey(csCiJob);
    }

    @Override
    public void deleteCsCiJobById(int id) {
        csCiJobMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CsCiJob queryCsCiJobById(int id) {
        return csCiJobMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CsCiJob> queryCsCiJobByScmMemberIdAndBranch(int scmMemberId, String branch) {
        Example example = new Example(CsCiJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("scmMemberId", scmMemberId);
        criteria.andEqualTo("branch", branch);
        return csCiJobMapper.selectByExample(example);
    }

    @Override
    public CsCiJob queryCsCiJobByUniqueKey(int applicationId, String jobKey) {
        Example example = new Example(CsCiJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        criteria.andEqualTo("jobKey", jobKey);
        return csCiJobMapper.selectOneByExample(example);
    }

    @Override
    public int countCsCiJobByApplicationId(int applicationId) {
        Example example = new Example(CsCiJob.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        return csCiJobMapper.selectCountByExample(example);
    }
}
