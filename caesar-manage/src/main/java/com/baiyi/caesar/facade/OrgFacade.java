package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.org.DepartmentMemberParam;
import com.baiyi.caesar.domain.param.org.DepartmentParam;
import com.baiyi.caesar.domain.vo.org.DepartmentTreeVO;
import com.baiyi.caesar.domain.vo.org.OrgDepartmentMemberVO;
import com.baiyi.caesar.domain.vo.org.OrgDepartmentVO;
import com.baiyi.caesar.domain.vo.org.OrgChartVO;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:36 下午
 * @Version 1.0
 */
public interface OrgFacade {

    DataTable<OrgDepartmentVO.Department> queryDepartmentPage(DepartmentParam.PageQuery pageQuery);

    DepartmentTreeVO.DepartmentTree queryDepartmentTree(int parentId);

    /**
     * 查询组织架构拓扑
     * @param parentId 当值为 -1 则按setting配置查询
     * @return
     */
    OrgChartVO.OrgChart queryOrgChart(int parentId);

    DepartmentTreeVO.DepartmentTree queryDepartmentTree();

    BusinessWrapper<Boolean> addDepartment(OrgDepartmentVO.Department department);

    OrgDepartmentVO.Department queryDepartmentById(int id);

    BusinessWrapper<Boolean> delDepartmentById(int id);

    BusinessWrapper<Boolean> updateDepartment(OrgDepartmentVO.Department department);

    BusinessWrapper<Boolean> dropDepartmentTree(int draggingParentId, int dropParentId, String dropType);

    DataTable<OrgDepartmentMemberVO.DepartmentMember> queryDepartmentMemberPage(DepartmentMemberParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addDepartmentMember(int departmentId, int userId);

    BusinessWrapper<Boolean> joinDepartmentMember(int departmentId);

    BusinessWrapper<Boolean> delDepartmentMemberById(int id);

    BusinessWrapper<Boolean> updateDepartmentMemberLeader(int id);

    BusinessWrapper<Boolean> updateDepartmentMemberApprovalAuthority(int id);

    /**
     * 删除用户的所有组织架构信息
     * @param userId
     * @return
     */
    BusinessWrapper<Boolean> delOrgUserById(int userId);

    BusinessWrapper<Boolean> checkUserInTheDepartment();

}
