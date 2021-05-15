package com.baiyi.caesar.packer.auth;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcAuthGroup;
import com.baiyi.caesar.domain.vo.auth.GroupVO;
import com.baiyi.caesar.domain.vo.auth.ResourceVO;
import com.baiyi.caesar.service.auth.OcAuthGroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/24 5:28 下午
 * @Version 1.0
 */
@Component
public class ResourcePacker {

    @Resource
    private OcAuthGroupService authGroupService;

    public ResourceVO.Resource wrap(ResourceVO.Resource resource) {
        // 装饰资源组
        if (resource.getGroupId() == null)
            return resource;
        OcAuthGroup ocAuthGroup = authGroupService.queryOcAuthGroupById(resource.getGroupId());
        resource.setGroupCode(ocAuthGroup.getGroupCode());
        resource.setGroup(BeanCopierUtil.copyProperties(ocAuthGroup, GroupVO.Group.class));
        return resource;
    }
}
