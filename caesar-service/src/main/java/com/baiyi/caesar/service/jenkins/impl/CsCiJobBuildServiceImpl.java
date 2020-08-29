package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.mapper.caesar.CsCiJobBuildMapper;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/5 2:10 下午
 * @Version 1.0
 */
@Service
public class CsCiJobBuildServiceImpl implements CsCiJobBuildService {

    @Resource
    private CsCiJobBuildMapper csCiJobBuildMapper;

    @Override
    public DataTable<CsCiJobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsCiJobBuild> list = csCiJobBuildMapper.queryCsCiJobByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<CsCiJobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query){
        PageHelper.startPage(1, query.getSize().intValue());
        return csCiJobBuildMapper.queryCsCiJobBuildArtifactParam(query);
    }

    @Override
    public CsCiJobBuild queryCiJobBuildById(int id) {
        return csCiJobBuildMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsCiJobBuild(CsCiJobBuild csCiJobBuild) {
        csCiJobBuildMapper.insert(csCiJobBuild);
    }

    @Override
    public void updateCsCiJobBuild(CsCiJobBuild csCiJobBuild) {
        csCiJobBuildMapper.updateByPrimaryKey(csCiJobBuild);
    }

    @Override
    public void deleteCsCiJobBuildById(int id) {
        csCiJobBuildMapper.deleteByPrimaryKey(id);
    }

    @Override
    public CsCiJobBuild queryCsCiJobBuildByUniqueKey(int ciJobId, int jobBuildNumber) {
        Example example = new Example(CsCiJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", ciJobId);
        criteria.andEqualTo("jobBuildNumber", jobBuildNumber);
        return csCiJobBuildMapper.selectOneByExample(example);
    }

    @Override
    public List<CsCiJobBuild> queryCsCiJobBuildByLastSize(int size) {
        Example example = new Example(CsCiJobBuild.class);
        example.setOrderByClause("job_build_number");
        PageHelper.startPage(1, size);
        return csCiJobBuildMapper.selectByExample(example);
    }

    @Override
    public List<CsCiJobBuild> queryCsCiJobBuildByFinalized(boolean isFinalized) {
        Example example = new Example(CsCiJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("finalized", isFinalized);
        return csCiJobBuildMapper.selectByExample(example);
    }

}
