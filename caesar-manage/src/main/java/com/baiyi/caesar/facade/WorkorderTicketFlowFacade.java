package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicket;

/**
 * @Author baiyi
 * @Date 2020/5/6 4:50 下午
 * @Version 1.0
 */
public interface WorkorderTicketFlowFacade {

    void createTicketFlow(OcWorkorderTicket ocWorkorderTicket);
}
