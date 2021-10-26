package com.baiyi.caesar.opscloud4.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/25 4:51 下午
 * @Version 1.0
 */
public class ServerModel {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class HttpResult implements Serializable {
        private static final long serialVersionUID = 8775334673786978117L;
        private Map<String, List<Server>> body;
        private boolean success;
        private String msg;
        private int code;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Server implements Serializable {

        private static final long serialVersionUID = 8186690303576793758L;
        private Integer id;
        private String name;
        private Integer serverGroupId;
        private String osType;
        private Integer envType;
        private String publicIp;
        private String privateIp;
        private Integer serverType;
        private String area;
        private Integer serialNumber;
        private Integer monitorStatus;
        private Boolean isActive;
        private Integer serverStatus;
        private Date createTime;
        private Date updateTime;
        private String comment;

    }

}
