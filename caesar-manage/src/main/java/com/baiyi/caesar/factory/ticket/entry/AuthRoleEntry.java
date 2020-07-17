package com.baiyi.caesar.factory.ticket.entry;

import com.baiyi.caesar.domain.vo.auth.RoleVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/27 6:10 下午
 * @Version 1.0
 */
@Builder
@Data
public class AuthRoleEntry implements ITicketEntry {

    private RoleVO.Role role;

    public String getName() {
        return this.role.getRoleName();
    }

}
