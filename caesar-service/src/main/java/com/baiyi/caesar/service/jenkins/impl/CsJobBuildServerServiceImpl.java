package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsJobBuildExecutor;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildServer;
import com.baiyi.caesar.mapper.caesar.CsJobBuildServerMapper;
import com.baiyi.caesar.service.jenkins.CsJobBuildServerService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/14 10:46 上午
 * @Version 1.0
 */
@Service
public class CsJobBuildServerServiceImpl implements CsJobBuildServerService {

    @Resource
    private CsJobBuildServerMapper csJobBuildServerMapper;

    @Override
    public List<CsJobBuildServer> queryCsJobBuildServerByBuildId(int buildType,int buildId) {
        Example example = new Example(CsJobBuildServer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("buildType", buildType);
        criteria.andEqualTo("buildId", buildId);
        return csJobBuildServerMapper.selectByExample(example);
    }


    @Override
    public void addCsJobBuildServer(CsJobBuildServer csJobBuildServer){
        csJobBuildServerMapper.insert(csJobBuildServer);
    }

    @Override
    public void deleteCsJobBuildServerById(int id){
        csJobBuildServerMapper.deleteByPrimaryKey(id);
    }


}
