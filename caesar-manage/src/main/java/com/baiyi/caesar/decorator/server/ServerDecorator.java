package com.baiyi.caesar.decorator.server;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
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
    private TagDecorator tagDecorator;

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
        // 装饰 标签
        server.setTags(tagDecorator.decorator(BusinessType.SERVER.getType(), server.getId()));
        return server;
    }

}
