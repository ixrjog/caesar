package com.baiyi.caesar.service.org;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartment;
import com.baiyi.caesar.domain.param.org.DepartmentParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:40 下午
 * @Version 1.0
 */
public interface OcOrgDepartmentService {

    DataTable<OcOrgDepartment> queryOcOrgDepartmentParam(DepartmentParam.PageQuery pageQuery);

    List<OcOrgDepartment> queryOcOrgDepartmentByParentId(int parentId);

    OcOrgDepartment queryOcOrgDepartmentById(int id);

    void addOcOrgDepartment(OcOrgDepartment ocOrgDepartment);

    void updateOcOrgDepartment(OcOrgDepartment ocOrgDepartment);

    void deleteOcOrgDepartmentById(int id);
}
