package com.baiyi.caesar.service.gitlab.impl;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.mapper.caesar.CsGitlabWebhookMapper;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
    public CsGitlabWebhook queryOneCsGitlabWebhookByObjectKind(String objectKind){
        Example example = new Example(CsGitlabWebhook.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("objectKind", objectKind);
        criteria.andEqualTo("isConsumed", false);
        example.setOrderByClause(" create_time");
        PageHelper.startPage(1, 1);
        return csGitlabWebhookMapper.selectOneByExample(example);
    }

    @Override
    public void addCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhookMapper.insert(csGitlabWebhook);
    }

    @Override
    public void updateCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhookMapper.updateByPrimaryKey(csGitlabWebhook);
    }
}
