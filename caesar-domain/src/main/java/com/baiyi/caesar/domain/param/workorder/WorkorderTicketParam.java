package com.baiyi.caesar.domain.param.workorder;

import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/28 7:03 下午
 * @Version 1.0
 */
public class WorkorderTicketParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class QueryMyTicketPage extends PageParam {

        @ApiModelProperty(value = "用户id")
        private Integer userId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class QueryTicketPage extends PageParam {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "工单阶段")
        private String ticketPhase;

        @ApiModelProperty(value = "工单状态")
        private Integer ticketStatus;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CreateTicket {
        @ApiModelProperty(value = "工单key")
        private String workorderKey;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryTicket {
        @ApiModelProperty(value = "workorderTicketId")
        private Integer id;
    }


}
