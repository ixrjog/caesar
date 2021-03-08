package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.bo.WorkorderTicketFlowBO;
import com.baiyi.caesar.common.base.TicketPhase;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorder;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicket;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketFlow;
import com.baiyi.caesar.facade.WorkorderTicketFlowFacade;
import com.baiyi.caesar.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.caesar.service.ticket.OcWorkorderTicketFlowService;
import com.baiyi.caesar.service.ticket.OcWorkorderTicketService;
import com.baiyi.caesar.service.workorder.OcWorkorderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:49 下午
 * @Version 1.0
 */
@Service
public class WorkorderTicketFlowFacadeImpl implements WorkorderTicketFlowFacade {

    @Resource
    protected OcWorkorderService ocWorkorderService;

    @Resource
    protected OcWorkorderTicketService ocWorkorderTicketService;

    @Resource
    protected OcWorkorderTicketFlowService ocWorkorderTicketFlowService;

    private OcWorkorder getOcWorkorderById(int workorderId) {
        return ocWorkorderService.queryOcWorkorderById(workorderId);
    }

    @Override
    public void createTicketFlow(OcWorkorderTicket ocWorkorderTicket) {
        // 提交工单
        WorkorderTicketFlowBO workorderTicketFlowBO = WorkorderTicketFlowBO.builder()
                .ticketId(ocWorkorderTicket.getId())
                .flowName(TicketPhase.APPLIED.getPhase())
                .comment("提交申请")
                .build();
        int flowParentId = saveWorkorderTicketFlow(workorderTicketFlowBO).getId();
        ocWorkorderTicket.setFlowId(flowParentId );
        ocWorkorderTicket.setStartTime(new Date());
        ocWorkorderTicketService.updateOcWorkorderTicket(ocWorkorderTicket);
        // 订阅用户发布消息
        WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.APPLIED.getPhase()).subscribe(ocWorkorderTicket);

        // 判断是否需要上级审批
        OcWorkorder ocWorkorder = getOcWorkorderById(ocWorkorderTicket.getWorkorderId());
        if (ocWorkorder.getOrgApproval()) {
            WorkorderTicketFlowBO workorderTicketFlowByOrgAppoval = WorkorderTicketFlowBO.builder()
                    .ticketId(ocWorkorderTicket.getId())
                    .flowName(TicketPhase.ORG_APPROVAL.getPhase())
                    .comment("上级审批")
                    .approvalType(0) // org审批
                    .flowParentId(flowParentId)
                    .build();
            OcWorkorderTicketFlow ocWorkorderTicketFlow = saveWorkorderTicketFlow(workorderTicketFlowByOrgAppoval);

            flowParentId = ocWorkorderTicketFlow.getId();
            // 订阅用户发布消息
            WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.ORG_APPROVAL.getPhase()).subscribe(ocWorkorderTicket);
        }

        // 判断是否需要审批组审批
        if (ocWorkorder.getApprovalGroupId() != 0) {
            WorkorderTicketFlowBO workorderTicketFlowByApprovalGroup = WorkorderTicketFlowBO.builder()
                    .ticketId(ocWorkorderTicket.getId())
                    .flowName(TicketPhase.USERGROUP_APPROVAL.getPhase())
                    .comment("用户审批")
                    .approvalType(1) // 审批组成员审批
                    .approvalGroupId(ocWorkorder.getApprovalGroupId())
                    .flowParentId(flowParentId)
                    .build();
            OcWorkorderTicketFlow ocWorkorderTicketFlow = saveWorkorderTicketFlow(workorderTicketFlowByApprovalGroup);
            flowParentId =ocWorkorderTicketFlow.getId();
            // 订阅用户发布消息
            WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(TicketPhase.USERGROUP_APPROVAL.getPhase()).subscribe(ocWorkorderTicket);
        }

        // 工单结束
        WorkorderTicketFlowBO workorderTicketFlowByFinalized = WorkorderTicketFlowBO.builder()
                .ticketId(ocWorkorderTicket.getId())
                .flowName(TicketPhase.FINALIZED.getPhase())
                .comment("工单结束")
                .flowParentId(flowParentId)
                .build();
        saveWorkorderTicketFlow(workorderTicketFlowByFinalized);
        // TODO 配置型工单 订阅用户(运维)发布消息
    }

    private OcWorkorderTicketFlow saveWorkorderTicketFlow(WorkorderTicketFlowBO workorderTicketFlowBO) {
        OcWorkorderTicketFlow ocWorkorderTicketFlow = BeanCopierUtil.copyProperties(workorderTicketFlowBO, OcWorkorderTicketFlow.class);
        ocWorkorderTicketFlowService.addOcWorkorderTicketFlow(ocWorkorderTicketFlow);
        return ocWorkorderTicketFlow;
    }


}
