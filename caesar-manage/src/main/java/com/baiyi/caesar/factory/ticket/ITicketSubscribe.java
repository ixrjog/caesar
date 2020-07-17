package com.baiyi.caesar.factory.ticket;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicket;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketSubscribe;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/6 1:28 下午
 * @Version 1.0
 */
public interface ITicketSubscribe {

    String getKey();

    OcWorkorderTicketSubscribe queryTicketSubscribe(OcWorkorderTicket ocWorkorderTicket, OcUser ocUser);

    /**
     * 订阅工单
     *
     * @param ocWorkorderTicket
     * @return
     */
    BusinessWrapper<Boolean> subscribe(OcWorkorderTicket ocWorkorderTicket);

    /**
     * 取消订阅
     *
     * @param ocWorkorderTicket
     * @return
     */
    void unsubscribe(OcWorkorderTicket ocWorkorderTicket);

    void invokeFlowStep(WorkorderTicketVO.Ticket ticket, String ticketPhase);

    /**
     * 当前步骤用户是否可审批
     * @param ocUser
     * @param ticket
     * @return
     */
    Boolean isAllowApproval(OcUser ocUser, WorkorderTicketVO.Ticket ticket);

    List<OcWorkorderTicketSubscribe> queryTicketSubscribes(WorkorderTicketVO.Ticket ticket);

}
