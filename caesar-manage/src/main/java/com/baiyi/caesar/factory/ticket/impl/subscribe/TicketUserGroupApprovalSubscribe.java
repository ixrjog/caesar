package com.baiyi.caesar.factory.ticket.impl.subscribe;

import com.baiyi.caesar.common.base.TicketPhase;
import com.baiyi.caesar.common.base.TicketSubscribeType;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.workorder.ApprovalStepsVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.caesar.factory.ticket.ITicketSubscribe;
import com.baiyi.caesar.service.workorder.OcWorkorderApprovalGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 8:08 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TicketUserGroupApprovalSubscribe extends BaseTicketSubscribe implements ITicketSubscribe {

    @Resource
    private OcWorkorderApprovalGroupService ocWorkorderApprovalGroupService;

    @Override
    public OcWorkorderTicketSubscribe queryTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser) {
        OcWorkorder ocWorkorder = getOcWorkorderById(ocWorkorderTicket.getWorkorderId());
        if (ocWorkorder.getApprovalGroupId() == 0) return null;
        return ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByParam(ocWorkorderTicket.getId(), ocUser.getId(), TicketSubscribeType.USERGROUP_APPROVAL.getType());
    }

    @Override
    public String getKey() {
        return TicketPhase.USERGROUP_APPROVAL.getPhase();
    }

    // 工单创建状态-进入审批阶段
    @Override
    public BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket) {
        OcWorkorder ocWorkorder = getOcWorkorderById(ocWorkorderTicket.getWorkorderId());
        addTicketSubscribe(ocWorkorder, ocWorkorderTicket);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase) {
        OcWorkorder ocWorkorder = getOcWorkorderById(ticket.getWorkorderId());
        if (IDUtil.isEmpty(ocWorkorder.getApprovalGroupId()))
            return;
        OcWorkorderApprovalGroup ocWorkorderApprovalGroup = ocWorkorderApprovalGroupService.queryOcWorkorderApprovalGroupById(ocWorkorder.getApprovalGroupId());
        ApprovalStepsVO.ApprovalStep approvalStep = ApprovalStepsVO.ApprovalStep.builder()
                .title(ocWorkorderApprovalGroup.getComment())
                .description(ocWorkorderApprovalGroup.getName())
                .build();
        ticket.getApprovalDetail().getApprovalSteps().add(approvalStep);

        if (TicketPhase.USERGROUP_APPROVAL.getPhase().equals(ticketPhase))
            ticket.getApprovalDetail().setActive(ticket.getApprovalDetail().getApprovalSteps().size());
    }

    @Override
    public List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket) {
        return ocWorkorderTicketSubscribeService.queryOcWorkorderTicketSubscribeByAppoval(ticket.getId(), TicketSubscribeType.USERGROUP_APPROVAL.getType());
    }

}
