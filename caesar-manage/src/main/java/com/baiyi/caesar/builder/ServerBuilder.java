package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.ServerBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.vo.server.ServerVO;

/**
 * @Author baiyi
 * @Date 2020/4/1 11:07 上午
 * @Version 1.0
 */
public class ServerBuilder {

    public static OcServer build(ServerVO.Server server) {
        ServerBO serverBO = ServerBO.builder()
                .name(server.getName())
                .serialNumber(server.getSerialNumber())
                .envType(server.getEnvType())
                .loginUser(server.getLoginUser())
                .loginType(server.getLoginType())
                .serverGroupId(server.getServerGroupId())
                .publicIp(server.getPublicIp())
                .privateIp(server.getPrivateIp())
                .serverType(server.getServerType())
                .area(server.getArea())
                .comment(server.getComment())
                .build();
        return covert(serverBO);
    }

    private static OcServer covert( ServerBO serverBO) {
        return BeanCopierUtil.copyProperties(serverBO, OcServer.class);
    }
}
