package com.baiyi.caesar.decorator.server;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.decorator.env.EnvDecorator;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.vo.server.ServerVO;
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
    private EnvDecorator envDecorator;

    @Resource
    private TagDecorator tagDecorator;

    @Resource
    private ServerGroupDecorator serverGroupDecorator;

    public ServerVO.Server decorator(ServerVO.Server server) {
        // 环境
        envDecorator.decorator(server);
        // 标签
        server.setBusinessType(BusinessType.SERVER.getType());
        tagDecorator.decorator(server);
        // 服务器组
        serverGroupDecorator.decorator(server);

        return server;
    }

}
