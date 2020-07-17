package com.baiyi.caesar.factory.ticket.entry;

import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/27 3:18 下午
 * @Version 1.0
 */
@Data
@Builder
public class ServerGroupEntry implements ITicketEntry {

    private ServerGroupVO.ServerGroup serverGroup;

    /**
     * 需要管理员账户权限
     **/
    @Builder.Default
    private Boolean needAdministratorAccount = false;

    public String getName() {
        return this.serverGroup.getName();
    }

}
