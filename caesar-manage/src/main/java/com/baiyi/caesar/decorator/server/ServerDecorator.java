package com.baiyi.caesar.decorator.server;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.base.BaseDecorator;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.service.server.OcServerService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:46 上午
 * @Version 1.0
 */
@Component("ServerDecorator")
public class ServerDecorator extends BaseDecorator {

    @Resource
    private OcServerService ocServerService;

    public ServerVO.Server decorator(ServerVO.Server server) {
        server.setBusinessType(BusinessType.SERVER.getType());
        decoratorServer(server);
        return server;
    }

    public void decorator(ServerVO.IServer iServer) {
        if (IDUtil.isEmpty(iServer.getServerId())) return;
        OcServer ocServer = ocServerService.queryOcServerById(iServer.getServerId());
        if (ocServer != null)
            iServer.setServer(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class));
    }

}
