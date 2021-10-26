package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.service.env.OcEnvService;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/30 2:53 下午
 * @Version 1.0
 */
@Component
public class ServerBaseFacade {

    private static OcEnvService ocEnvService;

    @Autowired
    private void setOcEnvService(OcEnvService ocEnvService) {
        ServerBaseFacade.ocEnvService = ocEnvService;
    }


    /**
     * 带列号
     *
     * @return
     */
    public static String acqServerName(ServerVO.Server server) {
        EnvVO.Env env = server.getEnv();
        if (env == null) {
            OcEnv ocEnv = ocEnvService.queryOcEnvByType(server.getEnvType());
            if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
                return Joiner.on("-").join(server.getName(), server.getSerialNumber());
            } else {
                return Joiner.on("-").join(server.getName(), ocEnv.getEnvName(), server.getSerialNumber());
            }
        } else {
            if (env == null || env.getEnvName().equals("prod")) {
                return Joiner.on("-").join(server.getName(), server.getSerialNumber());
            } else {
                return Joiner.on("-").join(server.getName(), env.getEnvName(), server.getSerialNumber());
            }
        }
    }

    /**
     * 带列号
     *
     * @return
     */
    public static String acqServerName(OcServer ocServer) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        return acqServerName(ocServer, ocEnv);
    }

    private static String acqServerName(OcServer ocServer, OcEnv ocEnv) {
        if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
            return Joiner.on("-").join(ocServer.getName(), ocServer.getSerialNumber());
        } else {
            return Joiner.on("-").join(ocServer.getName(), ocEnv.getEnvName(), ocServer.getSerialNumber());
        }
    }

    /**
     * 不带列号
     *
     * @return
     */
    public static String acqHostname(OcServer ocServer) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
            return ocServer.getName();
        } else {
            return Joiner.on("-").join(ocServer.getName(), ocEnv.getEnvName());
        }
    }
}
