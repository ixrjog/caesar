package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuildChange;
import com.baiyi.caesar.mapper.caesar.CsCiJobBuildChangeMapper;
import com.baiyi.caesar.service.jenkins.CsCiJobBuildChangeService;
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
public class CsCiJobBuildChangeServiceImpl implements CsCiJobBuildChangeService {

    @Resource
    private CsCiJobBuildChangeMapper csCiJobBuildChangeMapper;

    @Override
    public void addCsCiJobBuildChange(CsCiJobBuildChange csCiJobBuildChange) {
        csCiJobBuildChangeMapper.insert(csCiJobBuildChange);
    }

    @Override
    public void updateCsCiJobBuildChange(CsCiJobBuildChange csCiJobBuildChange) {
        csCiJobBuildChangeMapper.updateByPrimaryKey(csCiJobBuildChange);
    }

    @Override
    public List<CsCiJobBuildChange> queryCsCiJobBuildChangeByBuildId(int buildId) {
        Example example = new Example(CsCiJobBuildChange.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildId", buildId);
        return csCiJobBuildChangeMapper.selectByExample(example);
    }

    @Override
    public CsCiJobBuildChange queryCsCiJobBuildChangeByUniqueKey(int ciJobId, String commitId) {
        Example example = new Example(CsCiJobBuildChange.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ciJobId", ciJobId);
        criteria.andEqualTo("commitId", commitId);
        return csCiJobBuildChangeMapper.selectOneByExample(example);
    }
}
