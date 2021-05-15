package com.baiyi.caesar.factory.ticket.impl.subscribe;

import com.baiyi.caesar.common.base.TicketPhase;
import com.baiyi.caesar.common.base.TicketSubscribeType;
import com.baiyi.caesar.packer.department.DepartmentMemberDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorder;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicket;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketSubscribe;
import com.baiyi.caesar.domain.vo.org.OrgDepartmentMemberVO;
import com.baiyi.caesar.domain.vo.org.OrgApprovalVO;
import com.baiyi.caesar.domain.vo.workorder.ApprovalStepsVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.caesar.factory.ticket.ITicketSubscribe;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/6 1:41 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TicketOrgApprovalSubscribe extends BaseTicketSubscribe implements ITicketSubscribe {

    @Resource
    private DepartmentMemberDecorator departmentMemberDecorator;

    @Override
    public String getKey() {
        return TicketPhase.ORG_APPROVAL.getPhase();
    }

    @Override
    public OcWorkorderTicketSubscribe queryTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser) {
        OcWorkorder ocWorkorder = getOcWorkorderById(ocWorkorderTicket.getWorkorderId());
        if (!ocWorkorder.getOrgApproval()) return null;
        return ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByParam(ocWorkorderTicket.getId(), ocUser.getId(), TicketSubscribeType.ORG_APPROVAL.getType());
    }

    // 工单创建状态-进入审批阶段
    @Override
    public BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket) {
        OrgApprovalVO.OrgApproval orgApproval = departmentMemberDecorator.decorator(ocWorkorderTicket.getUserId());
        if (orgApproval.getIsApprovalAuthority()) {
            // 本人拥有审批权
            addTicketSubscribe(ocWorkorderTicket, ocWorkorderTicket.getUserId(), TicketSubscribeType.ORG_APPROVAL.getType());
        } else {
            if (orgApproval.getPreferenceDeptMember() != null) {
                addTicketSubscribe(ocWorkorderTicket, orgApproval.getPreferenceDeptMember().getUserId(), TicketSubscribeType.ORG_APPROVAL.getType());
            }
            if (orgApproval.getAlternativeDeptMembers() != null) {
                for (OrgDepartmentMemberVO.DepartmentMember member : orgApproval.getAlternativeDeptMembers())
                    addTicketSubscribe(ocWorkorderTicket, member.getUserId(), TicketSubscribeType.ORG_APPROVAL.getType());
            }
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase) {
        OrgApprovalVO.OrgApproval orgApproval = departmentMemberDecorator.decorator(ticket.getUserId());
        ApprovalStepsVO.ApprovalStep approvalStep = ApprovalStepsVO.ApprovalStep.builder()
                .title("上级审批")
                .description(getDescription(orgApproval))
                .build();
        ticket.getApprovalDetail().getApprovalSteps().add(approvalStep);

        if (TicketPhase.ORG_APPROVAL.getPhase().equals(ticketPhase))
            ticket.getApprovalDetail().setActive(ticket.getApprovalDetail().getApprovalSteps().size());
    }

    private String getDescription(OrgApprovalVO.OrgApproval orgApproval) {
        if (orgApproval.getIsError())
            return orgApproval.getErrorMsg();
        if (orgApproval.getIsApprovalAuthority())
            return "本人拥有审批权";
        if (orgApproval.getPreferenceDeptMember() != null)
            return orgApproval.getPreferenceDeptMember().getDisplayName();
        return Joiner.on(",").join(orgApproval.getAlternativeDeptMembers().stream().map(OrgDepartmentMemberVO.DepartmentMember::getDisplayName).collect(Collectors.toList()));
    }

    @Override
    public List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket) {
        return ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByAppoval(ticket.getId(), TicketSubscribeType.ORG_APPROVAL.getType());
    }

}
