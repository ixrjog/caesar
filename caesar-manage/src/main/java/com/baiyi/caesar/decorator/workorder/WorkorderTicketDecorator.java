package com.baiyi.caesar.decorator.workorder;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.base.TicketPhase;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.TimeAgoUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorder;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketEntry;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderVO;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.factory.ticket.ITicketHandler;
import com.baiyi.caesar.factory.ticket.ITicketSubscribe;
import com.baiyi.caesar.factory.ticket.WorkorderTicketFactory;
import com.baiyi.caesar.factory.ticket.WorkorderTicketSubscribeFactory;
import com.baiyi.caesar.service.ticket.OcWorkorderTicketEntryService;
import com.baiyi.caesar.service.workorder.OcWorkorderService;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/28 6:49 下午
 * @Version 1.0
 */
@Component
public class WorkorderTicketDecorator {

    @Resource
    private OcWorkorderTicketEntryService ocWorkorderTicketEntryService;

    @Resource
    private OcWorkorderService ocWorkorderService;

    @Resource
    private TicketApprovalDecorator ticketApprovalDecorator;

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    public WorkorderTicketVO.Ticket decorator(WorkorderTicketVO.Ticket ticket) {
        // 工单
        OcWorkorder ocWorkorder = ocWorkorderService.queryOcWorkorderById(ticket.getWorkorderId());
        ticket.setWorkorder(BeanCopierUtil.copyProperties(ocWorkorder, WorkorderVO.Workorder.class));
        // 发起人
        ticket.setUser(new GsonBuilder().create().fromJson(ticket.getUserDetail(), UserVO.User.class));
        if (ticket.getStartTime() != null)
            ticket.setAgo(TimeAgoUtil.format(ticket.getStartTime()));
        ticket.setIsInApproval(isInApproval(ticket));  // 审批流程中（给前端按钮使用）
        if (ticket.getIsInApproval()) { // 允许审批
            ITicketSubscribe iTicketSubscribe = WorkorderTicketSubscribeFactory.getTicketSubscribeByKey(ticket.getTicketPhase());
            ticket.setIsAllowApproval(iTicketSubscribe.isAllowApproval(userFacade.getOcUserBySession(), ticket));
        } else {
            ticket.setIsAllowApproval(false);
        }
        ticket.setIsAllowDelete(isAllowDelete(ticket));  // 允许删除
        // 工单条目
        List<OcWorkorderTicketEntry> ticketEntryList = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryByTicketId(ticket.getId());
        ticket.setTicketEntries(convertTicketEntries(ticketEntryList));
        ticketApprovalDecorator.decorator(ticket);
        return ticket;
    }

    private boolean isInApproval(WorkorderTicketVO.Ticket ticket) {
        // 审批结束 工单完成
        if (ticket.getTicketStatus() != 0) return false;
        if (ticket.getTicketPhase().equals(TicketPhase.CREATED.getPhase()))
            return false;
        if (ticket.getTicketPhase().equals(TicketPhase.FINALIZED.getPhase()))
            return false;
        if (ticket.getTicketPhase().equals(TicketPhase.CONFIGURATION.getPhase()))
            return false;
        return true;
    }

    /**
     * 已完成的工单不能删除
     * 管理员才能删除
     *
     * @param ticket
     * @return
     */
    private boolean isAllowDelete(WorkorderTicketVO.Ticket ticket) {
        // 审批结束 工单完成
        if (ticket.getTicketPhase().equals(TicketPhase.FINALIZED.getPhase()))
            return false;
        OcUser ocUser = userFacade.getOcUserBySession();
        BusinessWrapper<Boolean> wrapper = userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.ADMIN.getLevel());
        return wrapper.isSuccess();
    }

    private List<WorkorderTicketEntryVO.Entry> convertTicketEntries(List<OcWorkorderTicketEntry> ticketEntryList) {
        return ticketEntryList.stream().map(e -> {
            ITicketHandler ticketHandler = WorkorderTicketFactory.getTicketHandlerByKey(e.getEntryKey());
            return ticketHandler.convertTicketEntry(e);
        }).collect(Collectors.toList());
    }

}
