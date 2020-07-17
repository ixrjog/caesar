package com.baiyi.caesar.service.ansible.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcAnsibleScript;
import com.baiyi.caesar.domain.param.ansible.AnsibleScriptParam;
import com.baiyi.caesar.mapper.caesar.OcAnsibleScriptMapper;
import com.baiyi.caesar.service.ansible.OcAnsibleScriptService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/16 2:53 下午
 * @Version 1.0
 */
@Service
public class OcAnsibleScriptServiceImpl implements OcAnsibleScriptService {

    @Resource
    private OcAnsibleScriptMapper ocAnsibleScriptMapper;

    @Override
    public DataTable<OcAnsibleScript> queryOcAnsibleScriptByParam(AnsibleScriptParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcAnsibleScript> ocAnsibleScriptList = ocAnsibleScriptMapper.queryOcAnsibleScriptByParam(pageQuery);
        return new DataTable<>(ocAnsibleScriptList, page.getTotal());
    }

    @Override
    public OcAnsibleScript queryOcAnsibleScriptById(int id) {
        return ocAnsibleScriptMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcAnsibleScript(OcAnsibleScript ocAnsiblePlaybook) {
        ocAnsibleScriptMapper.insert(ocAnsiblePlaybook);
    }

    @Override
    public void updateOcAnsibleScript(OcAnsibleScript ocAnsiblePlaybook) {
        ocAnsibleScriptMapper.updateByPrimaryKey(ocAnsiblePlaybook);
    }

    @Override
    public void deleteOcAnsibleScriptById(int id) {
        ocAnsibleScriptMapper.deleteByPrimaryKey(id);
    }
}
