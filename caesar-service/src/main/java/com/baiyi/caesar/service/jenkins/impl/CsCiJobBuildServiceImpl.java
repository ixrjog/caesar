package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
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
        Page page = PageHelper.startPage(1, size);
        return csCiJobBuildMapper.selectByExample(example);
    }

}
