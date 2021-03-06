package com.baiyi.caesar.factory.ticket.impl.subscribe;

import com.baiyi.caesar.common.base.TicketPhase;
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
 * @Date 2020/5/7 9:27 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class TicketFinalizedSubscribe extends BaseTicketSubscribe implements ITicketSubscribe {

    @Override
    public String getKey() {
        return TicketPhase.FINALIZED.getPhase();
    }

    @Override
    public BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket) {
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase) {
        ApprovalStepsVO.ApprovalStep approvalStep = ApprovalStepsVO.ApprovalStep.builder()
                .title("完成")
                .description("工单结束")
                .build();
        ticket.getApprovalDetail().getApprovalSteps().add(approvalStep);
        if(TicketPhase.FINALIZED.getPhase().equals(ticketPhase))
            ticket.getApprovalDetail().setActive(ticket.getApprovalDetail().getApprovalSteps().size());
    }

    @Override
    public List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket){
        return Lists.newArrayList();
    }
}
