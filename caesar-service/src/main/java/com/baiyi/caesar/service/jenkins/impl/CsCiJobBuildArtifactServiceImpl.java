package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildArtifact;
import com.baiyi.caesar.mapper.caesar.CsCiJobBuildArtifactMapper;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildArtifactService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/7 5:57 下午
 * @Version 1.0
 */
@Service
public class CsCiJobBuildArtifactServiceImpl  implements CsCiJobBuildArtifactService {

    @Resource
    private CsCiJobBuildArtifactMapper csCiJobBuildArtifactMapper;

    @Override
    public void addCsCiJobBuildArtifact(CsCiJobBuildArtifact csCiJobBuildArtifact){
        csCiJobBuildArtifactMapper.insert(csCiJobBuildArtifact);
    }

    @Override
    public void updateCsCiJobBuildArtifact(CsCiJobBuildArtifact csCiJobBuildArtifact){
        csCiJobBuildArtifactMapper.updateByPrimaryKey(csCiJobBuildArtifact);
    }

    @Override
    public List<CsCiJobBuildArtifact> queryCsCiJobBuildArtifactByBuildId(int buildId) {
        Example example = new Example(CsCiJobBuildArtifact.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildId", buildId);
        return csCiJobBuildArtifactMapper.selectByExample(example);
    }
}
