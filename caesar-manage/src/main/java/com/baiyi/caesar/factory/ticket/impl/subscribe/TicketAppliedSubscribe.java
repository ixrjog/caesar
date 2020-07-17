package com.baiyi.caesar.factory.ticket.impl.subscribe;

import com.baiyi.caesar.common.base.TicketPhase;
import com.baiyi.caesar.common.base.TicketSubscribeType;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicket;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketSubscribe;
import com.baiyi.caesar.domain.vo.workorder.ApprovalStepsVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.caesar.factory.ticket.ITicketSubscribe;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:15 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class TicketAppliedSubscribe extends BaseTicketSubscribe implements ITicketSubscribe {

    @Override
    public String getKey() {
        return TicketPhase.APPLIED.getPhase();
    }

    @Override
    public BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket) {
        addTicketSubscribe(ocWorkorderTicket, ocWorkorderTicket.getUserId(), TicketSubscribeType.OWN.getType());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase) {
        ApprovalStepsVO.ApprovalStep approvalStep = ApprovalStepsVO.ApprovalStep.builder()
                .title("提交申请")
                .description("")
                .build();
        ticket.getApprovalDetail().getApprovalSteps().add(approvalStep);
        if(TicketPhase.APPLIED.getPhase().equals(ticketPhase))
            ticket.getApprovalDetail().setActive(ticket.getApprovalDetail().getApprovalSteps().size());
    }

    @Override
    public List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket){
        return Lists.newArrayList();
    }

}
