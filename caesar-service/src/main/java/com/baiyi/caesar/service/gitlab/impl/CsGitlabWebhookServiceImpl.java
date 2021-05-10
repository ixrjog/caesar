package com.baiyi.caesar.service.gitlab.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.domain.param.gitlab.GitlabEventParam;
import com.baiyi.caesar.mapper.caesar.CsGitlabWebhookMapper;
import com.baiyi.caesar.service.gitlab.CsGitlabWebhookService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/21 10:39 上午
 * @Version 1.0
 */
@Service
public class CsGitlabWebhookServiceImpl implements CsGitlabWebhookService {

    @Resource
    private CsGitlabWebhookMapper csGitlabWebhookMapper;

//    @Override
//    public CsGitlabWebhook queryOneCsGitlabWebhookByObjectKind(String objectKind) {
//        Example example = new Example(CsGitlabWebhook.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("objectKind", objectKind);
//        criteria.andEqualTo("isConsumed", false);
//        example.setOrderByClause(" create_time");
//        PageHelper.startPage(1, 1);
//        return csGitlabWebhookMapper.selectOneByExample(example);
//    }


    public CsGitlabWebhook getById(int id){
        return csGitlabWebhookMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhookMapper.insert(csGitlabWebhook);
    }

    @Override
    public void updateCsGitlabWebhook(CsGitlabWebhook csGitlabWebhook) {
        csGitlabWebhookMapper.updateByPrimaryKey(csGitlabWebhook);
    }

    @Override
    public DataTable<CsGitlabWebhook> queryCsGitlabWebhookByParam(GitlabEventParam.GitlabEventPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsGitlabWebhook> list = csGitlabWebhookMapper.queryCsGitlabWebhookByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }
}
