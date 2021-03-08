package com.baiyi.caesar.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.bo.WorkorderTicketEntryBO;
import com.baiyi.caesar.common.base.WorkorderKey;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcAuthRole;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.generator.caesar.OcUserGroup;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicketEntry;
import com.baiyi.caesar.domain.param.workorder.WorkorderTicketEntryParam;
import com.baiyi.caesar.domain.vo.auth.RoleVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.user.UserGroupVO;
import com.baiyi.caesar.domain.vo.workorder.WorkorderTicketEntryVO;
import com.baiyi.caesar.factory.ticket.entry.AuthRoleEntry;
import com.baiyi.caesar.factory.ticket.entry.ServerGroupEntry;
import com.baiyi.caesar.factory.ticket.entry.UserGroupEntry;

/**
 * @Author baiyi
 * @Date 2020/4/28 12:53 下午
 * @Version 1.0
 */
public class WorkorderTicketEntryBuilder {

    public static OcWorkorderTicketEntry build(WorkorderTicketEntryParam.TicketEntry ticketEntry, String name) {
        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketEntry.getTicketId())
                .name(name)
                .entryKey(ticketEntry.getEntryKey())
                .entryDetail(JSON.toJSONString(ticketEntry.getTicketEntry()))
                .build();
        return covert(workorderTicketEntryBO);
    }

    public static WorkorderTicketEntryVO.Entry build(int ticketId, OcServerGroup ocServerGroup) {
        ServerGroupEntry entry = ServerGroupEntry.builder()
                .serverGroup(BeanCopierUtil.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class))
                .build();

        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocServerGroup.getName())
                .businessId(ocServerGroup.getId())
                .entryKey(WorkorderKey.SERVER_GROUP.getKey())
                .comment(ocServerGroup.getComment())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }

    public static WorkorderTicketEntryVO.Entry build(int ticketId, OcAuthRole ocAuthRole) {
        AuthRoleEntry entry = AuthRoleEntry.builder()
                .role(BeanCopierUtil.copyProperties(ocAuthRole, RoleVO.Role.class))
                .build();

        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocAuthRole.getRoleName())
                .businessId(ocAuthRole.getId())
                .entryKey(WorkorderKey.AUTH_ROLE.getKey())
                .comment(ocAuthRole.getComment())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }

    public static WorkorderTicketEntryVO.Entry build(int ticketId, OcUserGroup ocUserGroup) {
        UserGroupEntry entry = UserGroupEntry.builder()
                .userGroup(BeanCopierUtil.copyProperties(ocUserGroup, UserGroupVO.UserGroup.class))
                .build();
        WorkorderTicketEntryBO workorderTicketEntryBO = WorkorderTicketEntryBO.builder()
                .workorderTicketId(ticketId)
                .name(ocUserGroup.getName())
                .businessId(ocUserGroup.getId())
                .entryKey(WorkorderKey.USER_GROUP.getKey())
                .comment(ocUserGroup.getComment())
                .ticketEntry(entry)
                .entryDetail(JSON.toJSONString(entry))
                .build();
        return covertVO(workorderTicketEntryBO);
    }

    private static WorkorderTicketEntryVO.Entry covertVO(WorkorderTicketEntryBO workorderTicketEntryBO) {
        return BeanCopierUtil.copyProperties(workorderTicketEntryBO, WorkorderTicketEntryVO.Entry.class);
    }

    private static OcWorkorderTicketEntry covert(WorkorderTicketEntryBO workorderTicketEntryBO) {
        return BeanCopierUtil.copyProperties(workorderTicketEntryBO, OcWorkorderTicketEntry.class);
    }
}
