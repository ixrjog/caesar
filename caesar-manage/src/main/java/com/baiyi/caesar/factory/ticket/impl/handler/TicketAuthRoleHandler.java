package com.baiyi.caesar.factory.ticket.impl.handler;

import com.baiyi.caesar.common.base.WorkorderKey;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketEntry;
import com.baiyi.caesar.domain.vo.auth.UserRoleVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.caesar.facade.AuthFacade;
import com.baiyi.caesar.factory.ticket.ITicketHandler;
import com.baiyi.caesar.factory.ticket.entry.AuthRoleEntry;
import com.baiyi.caesar.factory.ticket.entry.ITicketEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/27 6:08 下午
 * @Version 1.0
 */
@Slf4j
@Component("TicketAuthRoleExecutor")
public class TicketAuthRoleHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private AuthFacade authFacade;

    @Override
    public String getKey() {
        return WorkorderKey.AUTH_ROLE.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.AUTH_ROLE.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object  ticketEntry) {
        return new ObjectMapper().convertValue(ticketEntry, AuthRoleEntry.class);
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        return (T) new GsonBuilder().create().fromJson(ocWorkorderTicketEntry.getEntryDetail(), AuthRoleEntry.class);
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        AuthRoleEntry authRoleEntry = (AuthRoleEntry) entry;

        UserRoleVO.UserRole userRole = new UserRoleVO.UserRole();
        userRole.setRoleId(authRoleEntry.getRole().getId());
        userRole.setUsername(getUser(ocWorkorderTicketEntry.getWorkorderTicketId()).getUsername());

        authFacade.addUserRole(userRole);
        saveTicketEntry(ocWorkorderTicketEntry, BusinessWrapper.SUCCESS);
    }

    @Override
    protected BusinessWrapper<Boolean> updateTicketEntry(WorkorderTicketEntryVO.Entry entry) {
        return BusinessWrapper.SUCCESS;
    }
}
