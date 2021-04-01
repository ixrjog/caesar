package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGroupByHour;
import com.baiyi.caesar.domain.vo.dashboard.HotApplication;
import com.baiyi.caesar.domain.vo.dashboard.HotUser;
import com.baiyi.caesar.mapper.caesar.CsCiJobBuildMapper;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.RowBounds;
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
    public List<HotApplication> queryHotApplication(int length) {
        PageHelper.startPage(1, length);
        return csCiJobBuildMapper.queryHotApplication();
    }

    @Override
    public List<HotUser> queryHotUser(int length) {
        PageHelper.startPage(1, length);
        return csCiJobBuildMapper.queryHotUser();
    }

    @Override
    public List<BuildTaskGroupByHour> queryCiJobBuildGroupByHour() {
        return csCiJobBuildMapper.queryCiJobBuildGroupByHour();
    }

    @Override
    public List<CsCiJobBuild> queryLatestCsCiJobBuild(int length) {
        Example example = new Example(CsCiJobBuild.class);
        example.setOrderByClause(" create_time DESC");
        PageHelper.startPage(1, length);
        return csCiJobBuildMapper.selectByExample(example);
    }

    @Override
    public int countAllCsCiJobBuild() {
        return csCiJobBuildMapper.selectCountByExample(null);
    }

    @Override
    public DataTable<CsCiJobBuild> queryCiJobBuildPage(JobBuildParam.BuildPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(CsCiJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", pageQuery.getCiJobId());
        example.setOrderByClause("job_build_number desc");
        // List<CsCiJobBuild> list = csCiJobBuildMapper.queryCsCiJobByParam(pageQuery);
        return new DataTable<>(csCiJobBuildMapper.selectByExample(example), page.getTotal());
    }

    @Override
    public List<CsCiJobBuild> queryLatestCiJobBuildByCiJobId(int ciJobId) {
        Example example = new Example(CsCiJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", ciJobId);
        example.setOrderByClause(" job_build_number desc");
        PageHelper.startPage(1, 3);
        return csCiJobBuildMapper.selectByExample(example);
    }

    @Override
    public List<CsCiJobBuild> queryCiJobBuildByCiJobId(int ciJobId) {
        Example example = new Example(CsCiJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", ciJobId);
        return csCiJobBuildMapper.selectByExample(example);
    }

    @Override
    public List<CsCiJobBuild> queryMyCiJobBuild(String username,int size) {
        PageHelper.startPage(1, size);
        Example example = new Example(CsCiJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        example.setOrderByClause("create_time desc");
        return csCiJobBuildMapper.selectByExample(example);
    }

    @Override
    public List<CsCiJobBuild> queryCiJobBuildArtifact(JobBuildParam.JobBuildArtifactQuery query) {
        Example example = new Example(CsCiJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", query.getCiJobId());
        criteria.andEqualTo("buildStatus", "SUCCESS");
        example.setOrderByClause("job_build_number desc");
        return csCiJobBuildMapper.selectByExampleAndRowBounds(example, new RowBounds(0, query.getSize()));
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
