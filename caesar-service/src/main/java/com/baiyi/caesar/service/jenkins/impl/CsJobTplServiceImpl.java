package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.param.jenkins.JobTplParam;
import com.baiyi.caesar.mapper.caesar.CsJobTplMapper;
import com.baiyi.caesar.service.jenkins.CsJobTplService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/24 11:39 上午
 * @Version 1.0
 */
@Service
public class CsJobTplServiceImpl implements CsJobTplService {

    @Resource
    private CsJobTplMapper csJobTplMapper;

    @Override
    public DataTable<CsJobTpl> queryCsJobTplByParam(JobTplParam.JobTplPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsJobTpl> list = csJobTplMapper.queryCsJobTplByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addCsJobTpl(CsJobTpl csJobTpl) {
        csJobTplMapper.insert(csJobTpl);
    }

    @Override
    public void updateCsJobTpl(CsJobTpl csJobTpl) {
        csJobTplMapper.updateByPrimaryKey(csJobTpl);
    }

    @Override
    public void deleteCsJobTplById(int id) {
        csJobTplMapper.deleteByPrimaryKey(id);
    }
}
