package com.baiyi.caesar.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.bo.WorkorderTicketBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorder;
import com.baiyi.caesar.domain.generator.caesar.OcWorkorderTicket;

/**
 * @Author baiyi
 * @Date 2020/4/28 11:10 上午
 * @Version 1.0
 */
public class WorkorderTicketBuilder {

    public static OcWorkorderTicket build(OcUser ocUser, OcWorkorder ocWorkorder) {
        WorkorderTicketBO workorderTicketBO = WorkorderTicketBO.builder()
                .workorderId(ocWorkorder.getId())
                .userId(ocUser.getId())
                .username(ocUser.getUsername())
                .userDetail(JSON.toJSONString(ocUser))
                .build();
        return covert(workorderTicketBO);
    }

    private static OcWorkorderTicket covert(WorkorderTicketBO workorderTicketBO) {
        return BeanCopierUtil.copyProperties(workorderTicketBO, OcWorkorderTicket.class);
    }
}
