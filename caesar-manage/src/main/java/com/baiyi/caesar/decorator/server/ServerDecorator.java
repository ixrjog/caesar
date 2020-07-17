package com.baiyi.caesar.decorator.server;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.OcCloudServer;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.param.tag.TagParam;
import com.baiyi.caesar.domain.vo.cloud.CloudServerVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.facade.TagFacade;
import com.baiyi.caesar.service.cloud.OcCloudServerService;
import com.baiyi.caesar.service.env.OcEnvService;
import com.baiyi.caesar.service.server.OcServerGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:46 上午
 * @Version 1.0
 */
@Component("ServerDecorator")
public class ServerDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcCloudServerService ocCloudServerService;

    @Resource
    private TagFacade tagFacade;

    @Resource
    private ServerGroupDecorator serverGroupDecorator;

    public ServerVO.Server decorator(ServerVO.Server server) {
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(server.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            server.setEnv(env);
        }
        // 装饰 服务器组信息
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(server.getServerGroupId());
        if (ocServerGroup != null) {
            ServerGroupVO.ServerGroup serverGroup = BeanCopierUtils.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class);
            server.setServerGroup(serverGroupDecorator.decorator(serverGroup));
        }
        // 装饰云服务器
        OcCloudServer ocCloudServer = ocCloudServerService.queryOcCloudServerByUnqueKey(server.getServerType(), server.getId());
        if(ocCloudServer != null)
            server.setCloudServer(BeanCopierUtils.copyProperties(ocCloudServer, CloudServerVO.CloudServer.class));

        // 装饰 标签
        TagParam.BusinessQuery businessQuery = new TagParam.BusinessQuery();
        businessQuery.setBusinessType(BusinessType.SERVER.getType());
        businessQuery.setBusinessId(server.getId());
        server.setTags(tagFacade.queryBusinessTag(businessQuery));
        return server;
    }

}
