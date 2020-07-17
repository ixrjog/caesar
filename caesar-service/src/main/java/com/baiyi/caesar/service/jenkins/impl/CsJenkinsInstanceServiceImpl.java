package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.mapper.caesar.CsJenkinsInstanceMapper;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/17 3:45 下午
 * @Version 1.0
 */
@Service
public class CsJenkinsInstanceServiceImpl implements CsJenkinsInstanceService {

    @Resource
    private CsJenkinsInstanceMapper csJenkinsInstanceMapper;

    @Override
    public List<CsJenkinsInstance> queryAll() {
        return csJenkinsInstanceMapper.selectAll();
    }

}
