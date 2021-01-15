package com.baiyi.caesar.service.gitlab.impl;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabSystemHook;
import com.baiyi.caesar.mapper.caesar.CsGitlabSystemHookMapper;
import com.baiyi.caesar.service.gitlab.CsGitlabSystemHookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/12/22 1:51 下午
 * @Version 1.0
 */
@Service
public class CsGitlabSystemHookServiceImpl implements CsGitlabSystemHookService {

    @Resource
    private CsGitlabSystemHookMapper csGitlabSystemHookMapper;

    @Override
    public void addCsGitlabSystemHook(CsGitlabSystemHook csGitlabSystemHook) {
        csGitlabSystemHookMapper.insert(csGitlabSystemHook);
    }

    @Override
    public void updateCsGitlabSystemHook(CsGitlabSystemHook csGitlabSystemHook) {
        csGitlabSystemHookMapper.updateByPrimaryKey(csGitlabSystemHook);
    }
}
