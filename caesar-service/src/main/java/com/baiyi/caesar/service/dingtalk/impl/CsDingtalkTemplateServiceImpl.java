package com.baiyi.caesar.service.dingtalk.impl;

import com.baiyi.caesar.domain.generator.caesar.CsDingtalkTemplate;
import com.baiyi.caesar.mapper.caesar.CsDingtalkTemplateMapper;
import com.baiyi.caesar.service.dingtalk.CsDingtalkTemplateService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/8/14 5:35 下午
 * @Version 1.0
 */
@Service
public class CsDingtalkTemplateServiceImpl implements CsDingtalkTemplateService {

    @Resource
    private CsDingtalkTemplateMapper csDingtalkTemplateMapper;

    @Override
    public CsDingtalkTemplate queryCsDingtalkTemplateByUniqueKey(String jobType, int noticeType,int noticePhase) {
        Example example = new Example(CsDingtalkTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jobType", jobType);
        criteria.andEqualTo("noticeType", noticeType);
        criteria.andEqualTo("noticePhase",noticePhase);
        return csDingtalkTemplateMapper.selectOneByExample(example);
    }

    @Override
    public void addCsDingtalkTemplate(CsDingtalkTemplate csDingtalkTemplate) {
        csDingtalkTemplateMapper.insert(csDingtalkTemplate);
    }

    @Override
    public void updateCsDingtalkTemplate(CsDingtalkTemplate csDingtalkTemplate) {
        csDingtalkTemplateMapper.updateByPrimaryKey(csDingtalkTemplate);
    }

    @Override
    public void deleteCsDingtalkTemplateById(int id) {
        csDingtalkTemplateMapper.deleteByPrimaryKey(id);
    }
}
