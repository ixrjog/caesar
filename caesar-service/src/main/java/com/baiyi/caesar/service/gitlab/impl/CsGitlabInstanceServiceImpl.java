package com.baiyi.caesar.service.gitlab.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.param.gitlab.GitlabInstanceParam;
import com.baiyi.caesar.mapper.caesar.CsGitlabInstanceMapper;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:33 上午
 * @Version 1.0
 */
@Service
public class CsGitlabInstanceServiceImpl implements CsGitlabInstanceService {

    @Resource
    private CsGitlabInstanceMapper csGitlabInstanceMapper;

    @Override
    public List<CsGitlabInstance> queryAll() {
        return csGitlabInstanceMapper.selectAll();
    }

    @Override
    public CsGitlabInstance queryCsGitlabInstanceById(int id) {
        return csGitlabInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTable<CsGitlabInstance> queryCsGitlabInstanceByParam(GitlabInstanceParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsGitlabInstance> list = csGitlabInstanceMapper.queryCsGitlabInstanceByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addCsGitlabInstance(CsGitlabInstance csGitlabInstance) {
        csGitlabInstanceMapper.insert(csGitlabInstance);
    }

    @Override
    public void updateCsGitlabInstance(CsGitlabInstance csGitlabInstance) {
        csGitlabInstanceMapper.updateByPrimaryKey(csGitlabInstance);
    }

    @Override
    public void deleteCsGitlabInstanceById(int id) {
        csGitlabInstanceMapper.deleteByPrimaryKey(id);
    }
}
