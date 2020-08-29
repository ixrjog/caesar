package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildChange;
import com.baiyi.caesar.mapper.caesar.CsJobBuildChangeMapper;
import com.baiyi.caesar.service.jenkins.CsJobBuildChangeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/10 5:16 下午
 * @Version 1.0
 */
@Service
public class CsJobBuildChangeServiceImpl implements CsJobBuildChangeService {

    @Resource
    private CsJobBuildChangeMapper csJobBuildChangeMapper;

    @Override
    public void addCsJobBuildChange(CsJobBuildChange csJobBuildChange) {
        csJobBuildChangeMapper.insert(csJobBuildChange);
    }

    @Override
    public void updateCsJobBuildChange(CsJobBuildChange csJobBuildChange) {
        csJobBuildChangeMapper.updateByPrimaryKey(csJobBuildChange);
    }

    @Override
    public List<CsJobBuildChange> queryCsJobBuildChangeByBuildId(int buildType,int buildId) {
        Example example = new Example(CsJobBuildChange.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("buildId", buildId);
        return csJobBuildChangeMapper.selectByExample(example);
    }

    @Override
    public CsJobBuildChange queryCsJobBuildChangeByUniqueKey(int buildType,int jobId, String commitId) {
        Example example = new Example(CsJobBuildChange.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("jobId", jobId);
        criteria.andEqualTo("commitId", commitId);
        return csJobBuildChangeMapper.selectOneByExample(example);
    }
}
