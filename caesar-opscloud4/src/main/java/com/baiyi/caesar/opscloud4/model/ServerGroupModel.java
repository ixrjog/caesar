package com.baiyi.caesar.opscloud4.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/10/27 4:13 下午
 * @Version 1.0
 */
public class ServerGroupModel {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class HttpResult implements Serializable {
        private static final long serialVersionUID = 8775334673786978117L;
        private DataTable body;
        private boolean success;
        private String msg;
        private int code;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static  class DataTable {

        @ApiModelProperty(value = "分页数据")
        private List<ServerGroup> data;

        @ApiModelProperty(value = "当前页码")
        private int nowPage;

        @ApiModelProperty(value = "总记录数")
        private long totalNum;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServerGroup implements Serializable {

        private static final long serialVersionUID = -1726504511004650147L;
        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "组名称")
        @NotNull(message = "组名称不能为空")
        private String name;

        @ApiModelProperty(value = "组类型", example = "1")
        @NotNull(message = "组类型不能为空")
        private Integer serverGroupTypeId;

        @ApiModelProperty(value = "是否支持工单")
        @NotNull(message = "是否支持工单不能为空")
        private Boolean allowWorkorder;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        private Boolean isAdmin;

    }
    
}
