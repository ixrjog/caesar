package com.baiyi.caesar.factory.ticket;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketEntry;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketVO;

/**
 * @Author baiyi
 * @Date 2020/4/27 2:59 下午
 * @Version 1.0
 */
public interface ITicketHandler {

    /**
     * 执行工单条目
     **/
    void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry);

    BusinessWrapper<Boolean> addTicketEntry(OcUser ocUser,  WorkorderTicketEntryVO.Entry entry);

    BusinessWrapper<Boolean> updateTicketEntry(OcUser ocUser,  WorkorderTicketEntryVO.Entry entry);

    WorkorderTicketEntryVO.Entry convertTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry);

    WorkorderTicketVO.Ticket createTicket(OcUser ocUser);

    String getKey();

}
