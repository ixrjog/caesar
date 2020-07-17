package com.baiyi.caesar.domain.bo;

import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/1/10 1:45 下午
 * @Version 1.0
 */
@Builder
@Data
public class ServerBO {
    private OcServer ocServer;
    private OcEnv ocEnv;
}
