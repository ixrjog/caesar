package com.baiyi.caesar.factory.ticket.impl.handler;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.builder.UserPermissionBuilder;
import com.baiyi.caesar.common.base.WorkorderKey;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketEntry;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.caesar.facade.ServerGroupFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.factory.ticket.ITicketHandler;
import com.baiyi.caesar.factory.ticket.entry.ITicketEntry;
import com.baiyi.caesar.factory.ticket.entry.ServerGroupEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/27 5:10 下午
 * @Version 1.0
 */
@Slf4j
@Component("TicketServerGroupExecutor")
public class TicketServerGroupHandler<T> extends BaseTicketHandler<T> implements ITicketHandler {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Override
    public String getKey() {
        return WorkorderKey.SERVER_GROUP.getKey();
    }

    @Override
    protected String acqWorkorderKey() {
        return WorkorderKey.SERVER_GROUP.getKey();
    }

    @Override
    protected ITicketEntry acqITicketEntry(Object ticketEntry) {
        return new ObjectMapper().convertValue(ticketEntry, ServerGroupEntry.class);
    }

    @Override
    protected T getTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry) throws JsonSyntaxException {
        return (T) new GsonBuilder().create().fromJson(ocWorkorderTicketEntry.getEntryDetail(), ServerGroupEntry.class);
    }

    @Override
    protected void executorTicketEntry(OcWorkorderTicketEntry ocWorkorderTicketEntry, T entry) {
        ServerGroupEntry serverGroupEntry = (ServerGroupEntry) entry;
        ServerGroupParam.UserServerGroupPermission userServerGroupPermission = new ServerGroupParam.UserServerGroupPermission();
        userServerGroupPermission.setServerGroupId(serverGroupEntry.getServerGroup().getId());
        OcUser ocUser = getUser(ocWorkorderTicketEntry.getWorkorderTicketId());
        userServerGroupPermission.setUserId(ocUser.getId());
        BusinessWrapper<Boolean> wrapper = serverGroupFacade.grantUserServerGroup(userServerGroupPermission);
        // 管理员账户授权
        if (wrapper.isSuccess() && BooleanUtils.isTrue(serverGroupEntry.getNeedAdministratorAccount()))
            grantUserServerAdministratorAccount(ocUser, serverGroupEntry);
        saveTicketEntry(ocWorkorderTicketEntry, wrapper);
    }

    protected void grantUserServerAdministratorAccount(OcUser ocUser, ServerGroupEntry serverGroupEntry) {
        OcUserPermission ocUserPermission = UserPermissionBuilder.build(ocUser, serverGroupEntry);
        userPermissionFacade.addOcUserPermission(ocUserPermission);
    }

    @Override
    protected BusinessWrapper<Boolean> updateTicketEntry(WorkorderTicketEntryVO.Entry entry) {
        OcWorkorderTicketEntry ocWorkorderTicketEntry = ocWorkorderTicketEntryService.queryOcWorkorderTicketEntryById(entry.getId());
        ocWorkorderTicketEntry.setEntryDetail(JSON.toJSONString(entry.getTicketEntry()));
        ocWorkorderTicketEntryService.updateOcWorkorderTicketEntry(ocWorkorderTicketEntry);
        return BusinessWrapper.SUCCESS;
    }

}
