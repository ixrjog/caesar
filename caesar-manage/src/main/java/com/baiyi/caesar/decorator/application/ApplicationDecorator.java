package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.base.BusinessType;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.ApplicationServerGroupVO;
import com.baiyi.caesar.domain.vo.application.ApplicationVO;
import com.baiyi.caesar.domain.vo.dashboard.HotApplication;
import com.baiyi.caesar.domain.vo.tag.TagVO;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.service.application.CsApplicationScmGroupService;
import com.baiyi.caesar.service.application.CsApplicationScmMemberService;
import com.baiyi.caesar.service.application.CsApplicationServerGroupService;
import com.baiyi.caesar.service.gitlab.CsGitlabGroupService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
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
    private CsApplicationScmGroupService csApplicationScmGroupService;

    @Resource
    private CsGitlabGroupService csGitlabGroupService;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;
    @Resource
    private TagDecorator tagDecorator;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private CsApplicationServerGroupService csApplicationServerGroupService;

    public HotApplication decorator(HotApplication hotApplication) {
        List<CsApplicationScmMember> members = csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(hotApplication.getId());
        hotApplication.setTags(acqTags(members));
        return hotApplication;
    }

    private List<CsApplicationScmMember> convert(ApplicationVO.Application application, List<CsGitlabProject> projects) {
        return projects.stream().map(e -> {
            CsApplicationScmMember member = new CsApplicationScmMember();
            member.setApplicationId(application.getId());
            member.setScmType("GITLAB");
            member.setScmId(e.getId());
            member.setScmSshUrl(e.getSshUrl());
            member.setComment(e.getDescription());
            return member;
        }).collect(Collectors.toList());
    }

    public ApplicationVO.Application decorator(ApplicationVO.Application application, Integer extend) {
        if (extend == 0) return application;
        // 先判断是否绑定群组
       // List<CsApplicationScmGroup> groups = csApplicationScmGroupService.queryApplicationScmGroupByApplicationId(application.getId());
//        List<CsApplicationScmMember> members = Lists.newArrayList();
//        if (!CollectionUtils.isEmpty(groups)) {
//            for (CsApplicationScmGroup group : groups) {
//                CsGitlabGroup gitlabGroup = csGitlabGroupService.queryCsGitlabGroupById(group.getGroupId());
//                List<CsGitlabProject> projects = csGitlabProjectService.queryCsGitlabProjectByInstanceIdAndNamespacePath(gitlabGroup.getInstanceId(), gitlabGroup.getPath());
//                if (!CollectionUtils.isEmpty(projects))
//                    members.addAll(convert(application, projects));
//            }
//        } else {
//            members = csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(application.getId());
//        }
        List<CsApplicationScmMember>  members = csApplicationScmMemberService.queryCsApplicationScmMemberByApplicationId(application.getId());
        application.setTags(acqTags(members));
        application.setScmMembers(BeanCopierUtil.copyListProperties(members, ApplicationVO.ScmMember.class));
        List<CsApplicationServerGroup> csApplicationServerGroups = csApplicationServerGroupService.queryCsApplicationServerGroupByApplicationId(application.getId());
        if (!CollectionUtils.isEmpty(csApplicationServerGroups))
            application.setServerGroups(BeanCopierUtil.copyListProperties(csApplicationServerGroups, ApplicationServerGroupVO.ApplicationServerGroup.class));
        return application;
    }

    public ApplicationVO.Application decorator(ApplicationVO.Application application, OcUser ocUser, Integer extend) {
        application = decorator(application, extend);
        OcUserPermission ocUserPermission = userPermissionFacade.queryUserPermissionByUniqueKey(ocUser.getId(), BusinessType.APPLICATION.getType(), application.getId());
        application.setUserPermission(BeanCopierUtil.copyProperties(ocUserPermission, UserPermissionVO.UserPermission.class));
        return application;
    }

    private List<TagVO.Tag> acqTags(List<CsApplicationScmMember> members) {
        List<TagVO.Tag> tags = Lists.newArrayList();
        if (CollectionUtils.isEmpty(members)) return tags;
        Map<String, TagVO.Tag> tagMap = Maps.newHashMap();
        members.forEach(e ->
                tagMap.putAll(getTagMap(tagDecorator.acqTags(BusinessType.GITLAB_PROJECT.getType(), e.getScmId())))
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
