package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.mapper.caesar.CsJobBuildArtifactMapper;
import com.baiyi.caesar.service.jenkins.CsJobBuildArtifactService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/28 2:13 下午
 * @Version 1.0
 */
@Service
public class CsJobBuildArtifactServiceImpl implements CsJobBuildArtifactService {

    @Resource
    private CsJobBuildArtifactMapper csJobBuildArtifactMapper;

    @Override
    public void addCsJobBuildArtifact(CsJobBuildArtifact csJobBuildArtifact) {
        csJobBuildArtifactMapper.insert(csJobBuildArtifact);
    }

    @Override
    public void updateCsJobBuildArtifact(CsJobBuildArtifact csJobBuildArtifact) {
        csJobBuildArtifactMapper.updateByPrimaryKey(csJobBuildArtifact);
    }

    @Override
    public List<CsJobBuildArtifact> queryCsJobBuildArtifactByBuildId(int buildType, int buildId) {
        Example example = new Example(CsJobBuildArtifact.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("buildId", buildId);
        return csJobBuildArtifactMapper.selectByExample(example);
    }

    @Override
    public CsJobBuildArtifact queryCsJobBuildArtifactByUniqueKey(int buildType, int buildId, String artifactFileName) {
        Example example = new Example(CsJobBuildArtifact.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("buildId", buildId);
        criteria.andEqualTo("artifactFileName", artifactFileName);
        return csJobBuildArtifactMapper.selectOneByExample(example);
    }

    @Override
    public int countCsJobBuildArtifactByBuildId(int buildType, int buildId) {
        Example example = new Example(CsJobBuildArtifact.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("buildId", buildId);
        return csJobBuildArtifactMapper.selectCountByExample(example);
    }

    @Override
    public void deleteCsJobBuildArtifactById(int id) {
        csJobBuildArtifactMapper.deleteByPrimaryKey(id);
    }

}
