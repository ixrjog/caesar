package com.baiyi.caesar.service.gitlab.impl;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.mapper.caesar.CsGitlabWebhookMapper;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/21 10:39 上午
 * @Version 1.0
 */
@Service
public class CsGitlabWebhookServiceImpl implements CsGitlabWebhookService {

    @Resource
    private CsGitlabWebhookMapper csGitlabWebhookMapper;

    @Override
    public void addCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhookMapper.insert(csGitlabWebhook);
    }

    @Override
    public void updateCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhookMapper.updateByPrimaryKey(csGitlabWebhook);
    }
}
