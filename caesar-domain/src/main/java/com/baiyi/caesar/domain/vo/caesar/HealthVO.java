package com.baiyi.caesar.domain.vo.caesar;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/9/7 2:22 下午
 * @Version 1.0
 */
public class HealthVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Health {
        private String status;
    }
}
