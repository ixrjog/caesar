package com.baiyi.caesar.packer.application;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.ApplicationServerGroupVO;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.dashboard.HotApplication;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationServerGroupService;
import com.baiyi.caesar.packer.tag.TagPacker;
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
public class ApplicationPacker {

    @Resource
    private CsApplicationScmMemberService applicationScmMemberService;

    @Resource
    private TagPacker tagPacker;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private CsApplicationServerGroupService applicationServerGroupService;

    public HotApplication wrap(HotApplication hotApplication) {
        List<CsApplicationScmMember> members = applicationScmMemberService.queryCsApplicationScmMemberByApplicationId(hotApplication.getId());
        hotApplication.setTags(acqTags(members));
        return hotApplication;
    }

    public ApplicationVO.Application wrap(ApplicationVO.Application application, Integer extend) {
        if (extend == 0) return application;
        List<CsApplicationScmMember>  members = applicationScmMemberService.queryCsApplicationScmMemberByApplicationId(application.getId());
        application.setTags(acqTags(members));
        application.setScmMembers(BeanCopierUtil.copyListProperties(members, ApplicationVO.ScmMember.class));
        List<CsApplicationServerGroup> csApplicationServerGroups = applicationServerGroupService.queryCsApplicationServerGroupByApplicationId(application.getId());
        if (!CollectionUtils.isEmpty(csApplicationServerGroups))
            application.setServerGroups(BeanCopierUtil.copyListProperties(csApplicationServerGroups, ApplicationServerGroupVO.ApplicationServerGroup.class));
        return application;
    }

    public ApplicationVO.Application wrap(ApplicationVO.Application application, OcUser ocUser, Integer extend) {
        application = wrap(application, extend);
        OcUserPermission ocUserPermission = userPermissionFacade.queryUserPermissionByUniqueKey(ocUser.getId(), BusinessType.APPLICATION.getType(), application.getId());
        application.setUserPermission(BeanCopierUtil.copyProperties(ocUserPermission, UserPermissionVO.UserPermission.class));
        return application;
    }

    private List<TagVO.Tag> acqTags(List<CsApplicationScmMember> members) {
        List<TagVO.Tag> tags = Lists.newArrayList();
        if (CollectionUtils.isEmpty(members)) return tags;
        Map<String, TagVO.Tag> tagMap = Maps.newHashMap();
        members.forEach(e ->
                tagMap.putAll(getTagMap(tagPacker.acqTags(BusinessType.GITLAB_PROJECT.getType(), e.getScmId())))
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
