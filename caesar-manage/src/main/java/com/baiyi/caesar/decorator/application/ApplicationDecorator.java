package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserPermission;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/21 3:07 下午
 * @Version 1.0
 */
@Component
public class ApplicationDecorator {

    @Resource
    private CsApplicationScmMemberService csApplicationScmMemberService;

    @Resource
    private TagDecorator tagDecorator;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    public ApplicationVO.Application decorator(ApplicationVO.Application application, Integer extend) {
        if (extend == 0) return application;
        List<CsApplicationScmMember> members = csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(application.getId());
        application.setTags(acqTagsByMembers(members));
        application.setScmMembers(BeanCopierUtils.copyListProperties(members, ApplicationVO.ScmMember.class));
        return application;
    }

    public ApplicationVO.Application decorator(ApplicationVO.Application application, OcUser ocUser, Integer extend) {
        application = decorator(application, extend);
        OcUserPermission ocUserPermission = userPermissionFacade.queryUserPermissionByUniqueKey(ocUser.getId(), BusinessType.APPLICATION.getType(), application.getId());
        application.setUserPermission(BeanCopierUtils.copyProperties(ocUserPermission, UserPermissionVO.UserPermission.class));
        return application;
    }

    private List<TagVO.Tag> acqTagsByMembers(List<CsApplicationScmMember> members) {
        List<TagVO.Tag> tags = Lists.newArrayList();
        if (CollectionUtils.isEmpty(members)) return tags;
        Map<String, TagVO.Tag> tagMap = Maps.newHashMap();
        members.forEach(e ->
                tagMap.putAll(getTagMap(tagDecorator.decorator(BusinessType.GITLAB_PROJECT.getType(), e.getScmId())))
        );
        tagMap.keySet().forEach(k -> tags.add(tagMap.get(k)));
        return tags;
    }

    private Map<String, TagVO.Tag> getTagMap(List<TagVO.Tag> tags) {
        if (CollectionUtils.isEmpty(tags))
            return Maps.newHashMap();
        return tags.stream().collect(Collectors.toMap(TagVO.Tag::getTagKey, a -> a, (k1, k2) -> k1));
    }

}
