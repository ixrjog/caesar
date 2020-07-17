package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicket;
import com.baiyi.caesar.domain.param.workorder.WorkorderTicketParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcWorkorderTicketMapper extends Mapper<OcWorkorderTicket> {

    List<OcWorkorderTicket> queryMyTicketByParam(WorkorderTicketParam.QueryMyTicketPage pageQuery);

    List<OcWorkorderTicket> queryTicketByParam(WorkorderTicketParam.QueryTicketPage pageQuery);
}