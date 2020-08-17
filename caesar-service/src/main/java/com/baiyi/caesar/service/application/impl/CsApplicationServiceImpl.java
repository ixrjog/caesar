package com.baiyi.caesar.service.application.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import com.baiyi.caesar.mapper.caesar.CsApplicationMapper;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/21 2:53 下午
 * @Version 1.0
 */
@Service
public class CsApplicationServiceImpl implements CsApplicationService {

    @Resource
    private CsApplicationMapper csApplicationMapper;

    @Override
    public DataTable<CsApplication> queryCsApplicationByParam(ApplicationParam.ApplicationPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsApplication> list = csApplicationMapper.queryCsApplicationByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public DataTable<CsApplication> queryMyCsApplicationByParam(ApplicationParam.MyApplicationPageQuery pageQuery){
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsApplication> list = csApplicationMapper.queryMyCsApplicationByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public CsApplication queryCsApplicationById(int id) {
        return csApplicationMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsApplication(CsApplication csApplication) {
        csApplicationMapper.insert(csApplication);
    }

    @Override
    public void updateCsApplication(CsApplication csApplication) {
        csApplicationMapper.updateByPrimaryKey(csApplication);
    }

    @Override
    public void deleteCsApplicationById(int id) {
        csApplicationMapper.deleteByPrimaryKey(id);
    }

}
