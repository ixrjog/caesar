package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.bo.OrgDepartmentMemberBO;
import com.baiyi.caesar.common.base.SettingName;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.department.DepartmentDecorator;
import com.baiyi.caesar.decorator.department.DepartmentMemberDecorator;
import com.baiyi.caesar.decorator.department.OrgDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartment;
import com.baiyi.caesar.domain.generator.caesar.OcOrgDepartmentMember;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.param.org.DepartmentMemberParam;
import com.baiyi.caesar.domain.param.org.DepartmentParam;
import com.baiyi.caesar.domain.vo.org.DepartmentTreeVO;
import com.baiyi.caesar.domain.vo.org.OrgChartVO;
import com.baiyi.caesar.domain.vo.org.OrgDepartmentMemberVO;
import com.baiyi.caesar.domain.vo.org.OrgDepartmentVO;
import com.baiyi.caesar.domain.vo.tree.TreeVO;
import com.baiyi.caesar.facade.OrgFacade;
import com.baiyi.caesar.facade.SettingBaseFacade;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.service.org.OcOrgDepartmentMemberService;
import com.baiyi.caesar.service.org.OcOrgDepartmentService;
import com.baiyi.caesar.service.user.OcUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/20 6:36 下午
 * @Version 1.0
 */
@Service
public class OrgFacadeImpl implements OrgFacade {

    @Resource
    private OcOrgDepartmentService ocOrgDepartmentService;

    @Resource
    private OcOrgDepartmentMemberService ocOrgDepartmentMemberService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private DepartmentDecorator departmentDecorator;

    @Resource
    private OrgDecorator orgDecorator;

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    @Resource
    private UserFacade userFacade;

    @Resource
    private SettingBaseFacade settingFacade;

    public static final int ROOT_PARENT_ID = 0;

    public static final int QUERY_BY_SETTING = -1;

    /**
     * @param draggingParentId
     * @param dropParentId
     * @param dropType         before、after、inner
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> dropDepartmentTree(int draggingParentId, int dropParentId, String dropType) {
        if (draggingParentId == dropParentId)
            return BusinessWrapper.SUCCESS;
        if (draggingParentId == ROOT_PARENT_ID)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_CANNOT_DROP_ROOT);
        OcOrgDepartment draggingDept = ocOrgDepartmentService.queryOcOrgDepartmentById(draggingParentId);
        if (draggingDept == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        OcOrgDepartment dropDept = ocOrgDepartmentService.queryOcOrgDepartmentById(dropParentId);
        if (dropDept == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        switch (dropType) {
            case "before":
                return dropDepartmentBefore(draggingDept, dropDept);
            case "after":
                return dropDepartmentAfter(draggingDept, dropDept);
            case "inner":
                return dropDepartmentInner(draggingDept, dropDept);
            default:
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_DROP_ERROR);
        }
    }

    /**
     * 插入节点前面
     *
     * @param draggingDept
     * @param dropDept
     * @return
     */
    private BusinessWrapper<Boolean> dropDepartmentBefore(OcOrgDepartment draggingDept, OcOrgDepartment dropDept) {
        List<OcOrgDepartment> children = queryDepartmentByParentId(dropDept.getParentId());
        draggingDept.setParentId(dropDept.getParentId());
        int order = 1;
        for (OcOrgDepartment c : children) {
            if (c.getId() == dropDept.getId()) {
                draggingDept.setDeptOrder(order);
                ocOrgDepartmentService.updateOcOrgDepartment(draggingDept);
                order += 1;
            }
            c.setDeptOrder(order);
            ocOrgDepartmentService.updateOcOrgDepartment(c);
            order += 1;
        }
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 插入节点后面
     *
     * @param draggingDept
     * @param dropDept
     * @return
     */
    private BusinessWrapper<Boolean> dropDepartmentAfter(OcOrgDepartment draggingDept, OcOrgDepartment dropDept) {
        List<OcOrgDepartment> children = queryDepartmentByParentId(dropDept.getParentId());
        draggingDept.setParentId(dropDept.getParentId());
        int order = 1;
        for (OcOrgDepartment c : children) {
            c.setDeptOrder(order);
            ocOrgDepartmentService.updateOcOrgDepartment(c);
            order += 1;
            if (c.getId() == dropDept.getId()) {
                draggingDept.setDeptOrder(order);
                ocOrgDepartmentService.updateOcOrgDepartment(draggingDept);
                order += 1;
            }
        }
        return BusinessWrapper.SUCCESS;
    }

    /**
     * 插入节点内部
     *
     * @param draggingDept
     * @param dropDept
     * @return
     */
    private BusinessWrapper<Boolean> dropDepartmentInner(OcOrgDepartment draggingDept, OcOrgDepartment dropDept) {
        List<OcOrgDepartment> children = queryDepartmentByParentId(dropDept.getId());
        draggingDept.setParentId(dropDept.getId());
        draggingDept.setDeptOrder(0);
        if (!CollectionUtils.isEmpty(children)) {
            for (int i = 1; i < children.size() + 1; i++) {
                children.get(i).setDeptOrder(i);
                ocOrgDepartmentService.updateOcOrgDepartment(children.get(i));
            }
        }
        ocOrgDepartmentService.updateOcOrgDepartment(draggingDept);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DepartmentTreeVO.DepartmentTree queryDepartmentTree() {
        return queryDepartmentTree(ROOT_PARENT_ID);
    }

    @Override
    public BusinessWrapper<Boolean> addDepartment(OrgDepartmentVO.Department department) {
        OcOrgDepartment ocOrgDepartment = BeanCopierUtil.copyProperties(department, OcOrgDepartment.class);
        ocOrgDepartment.setDeptOrder(128);
        tryRootDept(ocOrgDepartment);
        ocOrgDepartment.setDeptHiding(0);
        ocOrgDepartmentService.addOcOrgDepartment(ocOrgDepartment);
        return BusinessWrapper.SUCCESS;
    }

    private void tryRootDept(OcOrgDepartment ocOrgDepartment) {
        if (IDUtil.isEmpty(ocOrgDepartment.getParentId()))
            ocOrgDepartment.setParentId(1);
        if (CollectionUtils.isEmpty(ocOrgDepartmentService.queryOcOrgDepartmentByParentId(0)))
            ocOrgDepartment.setParentId(0);
    }

    @Override
    public BusinessWrapper<Boolean> updateDepartment(OrgDepartmentVO.Department department) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(department.getId());
        ocOrgDepartment.setName(department.getName());
        ocOrgDepartment.setDeptType(department.getDeptType());
        ocOrgDepartment.setComment(department.getComment());
        ocOrgDepartmentService.updateOcOrgDepartment(ocOrgDepartment);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public OrgDepartmentVO.Department queryDepartmentById(int id) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(id);
        return BeanCopierUtil.copyProperties(ocOrgDepartment, OrgDepartmentVO.Department.class);
    }

    @Override
    public BusinessWrapper<Boolean> delDepartmentById(int id) {
        if (id == ROOT_PARENT_ID)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_CANNOT_DELETE_ROOT);
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(id);
        if (ocOrgDepartment == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        // 查询下级部门
        List<OcOrgDepartment> list = ocOrgDepartmentService.queryOcOrgDepartmentByParentId(id);
        if (list != null && !list.isEmpty())
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_SUB_DEPT_EXISTS);
        // 查询成员
        if (ocOrgDepartmentMemberService.countOcOrgDepartmentMemberByDepartmentId(id) > 0)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_IS_NOT_EMPTY);
        ocOrgDepartmentService.deleteOcOrgDepartmentById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<OrgDepartmentVO.Department> queryDepartmentPage(DepartmentParam.PageQuery pageQuery) {
        DataTable<OcOrgDepartment> table = ocOrgDepartmentService.queryOcOrgDepartmentParam(pageQuery);
        List<OrgDepartmentVO.Department> page = BeanCopierUtil.copyListProperties(table.getData(), OrgDepartmentVO.Department.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DepartmentTreeVO.DepartmentTree queryDepartmentTree(int parentId) {
        List<OcOrgDepartment> deptList = queryDepartmentByParentId(parentId);
        List<TreeVO.DeptTree> tree = departmentDecorator.deptListToTree(deptList);
        return DepartmentTreeVO.DepartmentTree.builder()
                .parentId(ROOT_PARENT_ID)
                .tree(tree)
                .build();
    }

    @Override
    public OrgChartVO.OrgChart queryOrgChart(int parentId) {
        try {
            if (parentId == QUERY_BY_SETTING) {
                String id = settingFacade.querySetting(SettingName.ORG_DEPT_ID);
                parentId = Integer.parseInt(id);
            }
            List<OcOrgDepartment> deptList = queryDepartmentByParentId(parentId);
            List<OrgChartVO.Children> children = orgDecorator.deptListToChart(deptList);
            OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(parentId);
            return OrgChartVO.OrgChart.builder()
                    .id(parentId)
                    .children(children)
                    .name(queryLeaderName(parentId))
                    .title(ocOrgDepartment.getName())
                    .build();
        } catch (Exception e) {
            return OrgChartVO.OrgChart.builder().build();
        }
    }

    private String queryLeaderName(int parentId) {
        OcOrgDepartmentMember ocOrgDepartmentMember = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByLeader(parentId);
        if (ocOrgDepartmentMember != null) {
            OcUser ocUser = ocUserService.queryOcUserById(ocOrgDepartmentMember.getUserId());
            if (ocUser != null)
                return ocUser.getDisplayName();
        }
        return "空缺";
    }

    private List<OcOrgDepartment> queryDepartmentByParentId(int parentId) {
        return ocOrgDepartmentService.queryOcOrgDepartmentByParentId(parentId);
    }

    @Override
    public DataTable<OrgDepartmentMemberVO.DepartmentMember> queryDepartmentMemberPage(DepartmentMemberParam.PageQuery pageQuery) {
        DataTable<OcOrgDepartmentMember> table = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberParam(pageQuery);
        List<OrgDepartmentMemberVO.DepartmentMember> page = BeanCopierUtil.copyListProperties(table.getData(), OrgDepartmentMemberVO.DepartmentMember.class);
        return new DataTable<>(page.stream().map(e -> departmentMemberDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addDepartmentMember(int departmentId, int userId) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(departmentId);
        if (ocOrgDepartment == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        OcUser ocUser = ocUserService.queryOcUserById(userId);
        if (ocUser == null)
            return new BusinessWrapper<>(ErrorEnum.USER_NOT_EXIST);

        OcOrgDepartmentMember pre = ocOrgDepartmentMemberService.getOcOrgDepartmentMemberByUniqueKey(departmentId, userId);
        if (pre != null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_ALREADY_EXISTS);

        OrgDepartmentMemberBO orgDepartmentMemberBO = OrgDepartmentMemberBO.builder()
                .departmentId(departmentId)
                .userId(userId)
                .username(ocUser.getUsername())
                .build();

        // 添加部门成员
        ocOrgDepartmentMemberService.addOcOrgDepartmentMember(BeanCopierUtil.copyProperties(orgDepartmentMemberBO, OcOrgDepartmentMember.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> joinDepartmentMember(int departmentId) {
        OcOrgDepartment ocOrgDepartment = ocOrgDepartmentService.queryOcOrgDepartmentById(departmentId);
        if (ocOrgDepartment == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_NOT_EXIST);
        if (ocOrgDepartment.getParentId() == ROOT_PARENT_ID)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_CANNOT_JOIN_ROOT);
        OcUser ocUser = userFacade.getOcUserBySession();
        if (ocUser == null)
            return new BusinessWrapper<>(ErrorEnum.USER_NOT_EXIST);
        List<OcOrgDepartmentMember> members = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByUserId(ocUser.getId());
        for (OcOrgDepartmentMember member : members) {
            if (member.getIsLeader() == 1)
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_IS_LEADER);
            if (member.getIsApprovalAuthority() == 1)
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_IS_APPROVAL);
            ocOrgDepartmentMemberService.deleteOcOrgDepartmentMemberById(member.getId());
        }
        OrgDepartmentMemberBO orgDepartmentMemberBO = OrgDepartmentMemberBO.builder()
                .departmentId(departmentId)
                .userId(ocUser.getId())
                .username(ocUser.getUsername())
                .build();
        ocOrgDepartmentMemberService.addOcOrgDepartmentMember(BeanCopierUtil.copyProperties(orgDepartmentMemberBO, OcOrgDepartmentMember.class));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delDepartmentMemberById(int id) {
        ocOrgDepartmentMemberService.deleteOcOrgDepartmentMemberById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateDepartmentMemberLeader(int id) {
        OcOrgDepartmentMember member = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberById(id);
        if (member == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_NOT_EXIST);
        member.setIsLeader(member.getIsLeader() == 0 ? 1 : 0);
        ocOrgDepartmentMemberService.updateOcOrgDepartmentMember(member);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> updateDepartmentMemberApprovalAuthority(int id) {
        OcOrgDepartmentMember member = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberById(id);
        if (member == null)
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_NOT_EXIST);
        member.setIsApprovalAuthority(member.getIsApprovalAuthority() == 0 ? 1 : 0);
        ocOrgDepartmentMemberService.updateOcOrgDepartmentMember(member);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> delOrgUserById(int userId) {
        List<OcOrgDepartmentMember> members = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByUserId(userId);
        if (members != null)
            try {
                members.forEach(e -> ocOrgDepartmentMemberService.deleteOcOrgDepartmentMemberById(e.getId()));
            } catch (Exception e) {
                return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_MEMBER_DELETE_ERROR);
            }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> checkUserInTheDepartment() {
        List<OcOrgDepartmentMember> members = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByUserId(userFacade.getOcUserBySession().getId());
        if (CollectionUtils.isEmpty(members))
            return new BusinessWrapper<>(ErrorEnum.ORG_DEPARTMENT_USER_NOT_IN_THE_DEPT);
        return BusinessWrapper.SUCCESS;
    }


}
