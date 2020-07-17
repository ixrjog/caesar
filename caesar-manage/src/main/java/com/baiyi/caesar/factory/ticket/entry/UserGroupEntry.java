package com.baiyi.caesar.factory.ticket.entry;

import com.baiyi.caesar.domain.vo.user.UserGroupVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/27 6:31 下午
 * @Version 1.0
 */
@Builder
@Data
public class UserGroupEntry implements ITicketEntry{

    private UserGroupVO.UserGroup userGroup;

    public String getName() {
        return this. userGroup.getName();
    }

}
