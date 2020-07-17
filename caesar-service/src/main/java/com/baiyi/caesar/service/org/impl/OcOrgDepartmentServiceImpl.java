package com.baiyi.caesar.service.org.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartment;
import com.baiyi.caesar.domain.param.org.DepartmentParam;
import com.baiyi.caesar.mapper.caesar.OcOrgDepartmentMapper;
import com.baiyi.caesar.service.org.OcOrgDepartmentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:42 下午
 * @Version 1.0
 */
@Service
public class OcOrgDepartmentServiceImpl implements OcOrgDepartmentService {

    @Resource
    private OcOrgDepartmentMapper ocOrgDepartmentMapper;

    @Override
    public DataTable<OcOrgDepartment> queryOcOrgDepartmentParam(DepartmentParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcOrgDepartment> list = ocOrgDepartmentMapper.queryOcOrgDepartmentParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcOrgDepartment> queryOcOrgDepartmentByParentId(int parentId) {
        Example example = new Example(OcOrgDepartment.class);
        example.setOrderByClause("dept_order");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", parentId);
        return ocOrgDepartmentMapper.selectByExample(example);
    }

    @Override
    public OcOrgDepartment queryOcOrgDepartmentById(int id) {
        return ocOrgDepartmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcOrgDepartment(OcOrgDepartment ocOrgDepartment) {
        ocOrgDepartmentMapper.insert(ocOrgDepartment);
    }

    @Override
    public void updateOcOrgDepartment(OcOrgDepartment ocOrgDepartment) {
        ocOrgDepartmentMapper.updateByPrimaryKey(ocOrgDepartment);
    }

    @Override
    public void deleteOcOrgDepartmentById(int id) {
        ocOrgDepartmentMapper.deleteByPrimaryKey(id);
    }

}
