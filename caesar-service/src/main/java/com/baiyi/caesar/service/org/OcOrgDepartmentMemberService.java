package com.baiyi.caesar.service.org;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartmentMember;
import com.baiyi.caesar.domain.param.org.DepartmentMemberParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/21 4:24 下午
 * @Version 1.0
 */
public interface OcOrgDepartmentMemberService {

    DataTable<OcOrgDepartmentMember> queryOcOrgDepartmentMemberParam(DepartmentMemberParam.PageQuery pageQuery);

    void addOcOrgDepartmentMember(OcOrgDepartmentMember ocOrgDepartmentMember);

    void updateOcOrgDepartmentMember(OcOrgDepartmentMember ocOrgDepartmentMember);

    OcOrgDepartmentMember getOcOrgDepartmentMemberByUniqueKey(int departmentId, int userId);

    int countOcOrgDepartmentMemberByDepartmentId(int departmentId);

    List<OcOrgDepartmentMember> queryOcOrgDepartmentMemberByDepartmentId(int departmentId);

    /**
     * 查询部门中的leader
     * @param departmentId
     * @return
     */
    OcOrgDepartmentMember queryOcOrgDepartmentMemberByLeader(int departmentId);

    OcOrgDepartmentMember queryOcOrgDepartmentMemberById(int id);

    void deleteOcOrgDepartmentMemberById(int id);

    List<OcOrgDepartmentMember> queryOcOrgDepartmentMemberByUserId(int userId);
}
