package com.baiyi.caesar.workorder;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.packer.department.DepartmentMemberDecorator;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorder;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderApprovalMember;
import com.baiyi.caesar.domain.vo.org.OrgApprovalVO;
import com.baiyi.caesar.service.ticket.OcWorkorderApprovalMemberService;
import com.baiyi.caesar.service.user.OcUserService;
import com.baiyi.caesar.service.workorder.OcWorkorderService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/30 11:37 上午
 * @Version 1.0
 */
public class WorkorderTest extends BaseUnit {

    @Resource
    private OcWorkorderService ocWorkorderService;

    @Resource
    private OcWorkorderApprovalMemberService ocWorkorderApprovalMemberService;

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    @Resource
    private OcUserService ocUserService;

    @Test
    void updateUsersUuid() {
        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(19);

        //  ApprovalOptionsVO.ApprovalOptions options = WorkorderUtils.convert(ocWorkorder.getApprovalDetail());

        //System.err.println(JSON.toJSONString(options));

    }

    @Test
    void initTest() {
        // shanma wangzha jiji
        OcUser ocUser = ocUserService.queryOcUserByUsername("jiji");

        OcWorkorderApprovalMember memeber = new OcWorkorderApprovalMember();
        memeber.setGroupId(19);
        memeber.setUserId(ocUser.getId());
        memeber.setUsername(ocUser.getUsername());
        memeber.setComment(ocUser.getDisplayName());


        ocWorkorderApprovalMemberService.addOcWorkorderApprovalMember(memeber);

    }

    @Test
    void deptTest() {
        // banmayu
        OcUser ocUser = ocUserService.queryOcUserByUsername("banmayu");
        OrgApprovalVO.OrgApproval orgApproval = departmentMemberDecorator.decorator(ocUser.getId());
        System.err.println(JSON.toJSONString(orgApproval));

    }
}
