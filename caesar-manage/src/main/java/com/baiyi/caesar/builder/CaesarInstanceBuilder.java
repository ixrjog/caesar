package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.CaesarInstanceBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsInstance;

import java.net.InetAddress;

/**
 * @Author baiyi
 * @Date 2020/9/7 1:52 下午
 * @Version 1.0
 */
public class CaesarInstanceBuilder {

    public static CsInstance build(InetAddress inetAddress) {

        CaesarInstanceBO bo = CaesarInstanceBO.builder()
                .hostIp(inetAddress.getHostAddress())
                .hostName(inetAddress.getHostName())
                .name(inetAddress.getCanonicalHostName())

                .build();
        return covert(bo);
    }

    private static CsInstance covert(CaesarInstanceBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsInstance.class);
    }
}
