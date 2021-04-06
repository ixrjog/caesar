package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGroupByHour;
import com.baiyi.caesar.mapper.caesar.CsCdJobBuildMapper;
import com.baiyi.caesar.service.jenkins.CsCdJobBuildService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/29 2:21 下午
 * @Version 1.0
 */
@Service
public class CsCdJobBuildServiceImpl implements CsCdJobBuildService {

    @Resource
    private CsCdJobBuildMapper csCdJobBuildMapper;

    @Override
    public List<BuildTaskGroupByHour> queryCdJobBuildGroupByHour() {
        return csCdJobBuildMapper.queryCdJobBuildGroupByHour();
    }

    @Override
    public List<CsCdJobBuild> queryLatestCsCdJobBuild(int length) {
        Example example = new Example(CsCdJobBuild.class);
        example.setOrderByClause(" create_time DESC");
        PageHelper.startPage(1, length);
        return csCdJobBuildMapper.selectByExample(example);
    }

    @Override
    public int countAllCsCdJobBuild() {
        return csCdJobBuildMapper.selectCountByExample(null);
    }

    @Override
    public DataTable<CsCdJobBuild> queryCdJobBuildPage(JobDeploymentParam.DeploymentPageQuery pageQuery) {
        Example example = new Example(CsCdJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cdJobId", pageQuery.getCdJobId());
        example.setOrderByClause(" job_build_number DESC");
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        return new DataTable<>(csCdJobBuildMapper.selectByExample(example), page.getTotal());
    }

    @Override
    public List<CsCdJobBuild> queryCdJobBuildByCdJobId(int cdJobId) {
        Example example = new Example(CsCdJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cdJobId", cdJobId);
        return csCdJobBuildMapper.selectByExample(example);
    }

    @Override
    public List<CsCdJobBuild> queryLastCdJobBuild(int cdJobId) {
        Example example = new Example(CsCdJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cdJobId", cdJobId);
        example.setOrderByClause(" job_build_number DESC");
        PageHelper.startPage(1, 3);
        return csCdJobBuildMapper.selectByExample(example);
    }

    @Override
    public CsCdJobBuild queryCdJobBuildById(int id) {
        return csCdJobBuildMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsCdJobBuild(CsCdJobBuild csCdJobBuild) {
        csCdJobBuildMapper.insert(csCdJobBuild);
    }

    @Override
    public void updateCsCdJobBuild(CsCdJobBuild csCdJobBuild) {
        csCdJobBuildMapper.updateByPrimaryKey(csCdJobBuild);
    }

    @Override
    public void deleteCsCdJobBuildById(int id) {
        csCdJobBuildMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<CsCdJobBuild> queryMyCdJobBuild(String username, int size){
        PageHelper.startPage(1, size);
        Example example = new Example(CsCdJobBuild.class);
        if (!StringUtils.isEmpty(username)) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username", username);
        }
        example.setOrderByClause("create_time desc");
        return csCdJobBuildMapper.selectByExample(example);
    }

    @Override
    public CsCdJobBuild queryCsCdJobBuildByUniqueKey(int cdJobId, int jobBuildNumber) {
        Example example = new Example(CsCdJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cdJobId", cdJobId);
        criteria.andEqualTo("jobBuildNumber", jobBuildNumber);
        return csCdJobBuildMapper.selectOneByExample(example);
    }

    @Override
    public List<CsCdJobBuild> queryCsCdJobBuildByLastSize(int size) {
        Example example = new Example(CsCdJobBuild.class);
        example.setOrderByClause("job_build_number");
        PageHelper.startPage(1, size);
        return csCdJobBuildMapper.selectByExample(example);
    }

    @Override
    public List<CsCdJobBuild> queryCsCdJobBuildByFinalized(boolean isFinalized) {
        Example example = new Example(CsCdJobBuild.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("finalized", isFinalized);
        return csCdJobBuildMapper.selectByExample(example);
    }
}
