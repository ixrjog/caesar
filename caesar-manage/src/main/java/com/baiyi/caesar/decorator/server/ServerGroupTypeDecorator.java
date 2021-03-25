package com.baiyi.caesar.decorator.server;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroupType;
import com.baiyi.caesar.domain.vo.server.ServerGroupTypeVO;
import com.baiyi.caesar.service.server.OcServerGroupTypeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/3/25 1:42 下午
 * @Version 1.0
 */
@Component()
public class ServerGroupTypeDecorator {

    @Resource
    private OcServerGroupTypeService ocServerGroupTypeService;

    public void decorator(ServerGroupTypeVO.IServerGroupType iServerGroupType) {
        OcServerGroupType ocServerGroupType = ocServerGroupTypeService.queryOcServerGroupTypeByGrpType(iServerGroupType.getGrpType());
        if (ocServerGroupType == null) return;
        ServerGroupTypeVO.ServerGroupType serverGroupType = BeanCopierUtil.copyProperties(ocServerGroupType, ServerGroupTypeVO.ServerGroupType.class);
        iServerGroupType.setServerGroupType(serverGroupType);
    }
}
