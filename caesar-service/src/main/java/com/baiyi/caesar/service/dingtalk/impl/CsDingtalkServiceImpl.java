package com.baiyi.caesar.service.dingtalk.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsDingtalk;
import com.baiyi.caesar.domain.param.dingtalk.DingtalkParam;
import com.baiyi.caesar.mapper.caesar.CsDingtalkMapper;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:23 下午
 * @Version 1.0
 */
@Service
public class CsDingtalkServiceImpl implements CsDingtalkService {

    @Resource
    private CsDingtalkMapper csDingtalkMapper;

    @Override
    public DataTable<CsDingtalk> queryCsDingtalkByParam(DingtalkParam.DingtalkPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsDingtalk> list = csDingtalkMapper.queryCsDingtalkByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public CsDingtalk queryCsDingtalkById(int id) {
        return csDingtalkMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsDingtalk(CsDingtalk csDingtalk) {
        csDingtalkMapper.insert(csDingtalk);
    }

    @Override
    public void updateCsDingtalk(CsDingtalk csDingtalk) {
        csDingtalkMapper.updateByPrimaryKey(csDingtalk);
    }

    @Override
    public void deleteCsDingtalkById(int id) {
        csDingtalkMapper.deleteByPrimaryKey(id);
    }
}
