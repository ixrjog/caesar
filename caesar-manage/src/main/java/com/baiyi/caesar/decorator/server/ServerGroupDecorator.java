package com.baiyi.caesar.decorator.server;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroupType;
import com.baiyi.caesar.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.service.server.OcServerGroupService;
import com.baiyi.caesar.service.server.OcServerGroupTypeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/22 10:47 上午
 * @Version 1.0
 */
@Component("ServerGroupDecorator")
public class ServerGroupDecorator {

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcServerGroupTypeService ocServerGroupTypeService;

    public ServerGroupVO.ServerGroup decorator(ServerGroupVO.ServerGroup serverGroup) {
        OcServerGroupType ocServerGroupType = ocServerGroupTypeService.queryOcServerGroupTypeByGrpType(serverGroup.getGrpType());
        ServerGroupTypeVO.ServerGroupType serverGroupType = BeanCopierUtil.copyProperties(ocServerGroupType, ServerGroupTypeVO.ServerGroupType.class);
        serverGroup.setServerGroupType(serverGroupType);
        return serverGroup;
    }


    public void decorator(ServerGroupVO.IServerGroup iServerGroup) {
        // 装饰 服务器组信息
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(iServerGroup.getServerGroupId());
        if (ocServerGroup == null) return;
        ServerGroupVO.ServerGroup serverGroup = BeanCopierUtil.copyProperties(ocServerGroup, ServerGroupVO.ServerGroup.class);
        iServerGroup.setServerGroup(serverGroup);
        OcServerGroupType ocServerGroupType = ocServerGroupTypeService.queryOcServerGroupTypeByGrpType(serverGroup.getGrpType());
        ServerGroupTypeVO.ServerGroupType serverGroupType = BeanCopierUtil.copyProperties(ocServerGroupType, ServerGroupTypeVO.ServerGroupType.class);
        serverGroup.setServerGroupType(serverGroupType);
    }
}
