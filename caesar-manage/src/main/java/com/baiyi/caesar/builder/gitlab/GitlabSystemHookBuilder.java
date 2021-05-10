package com.baiyi.caesar.builder.gitlab;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.bo.gitlab.GitlabSystemHookBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabSystemHook;
import com.baiyi.caesar.domain.vo.gitlab.GitlabHookVO;

/**
 * @Author baiyi
 * @Date 2020/12/22 2:01 下午
 * @Version 1.0
 */
public class GitlabSystemHookBuilder {

    public static CsGitlabSystemHook build(CsGitlabInstance csGitlabInstance, GitlabHookVO.SystemHook systemHook) {
        GitlabSystemHookBO bo = GitlabSystemHookBO.builder()
                .instanceId(csGitlabInstance.getId())
                .projectId(systemHook.getProjectId())
                .groupId(systemHook.getGroupId())
                .name(systemHook.getName())
                .eventName(systemHook.getEventName())
                .hooksContent(JSON.toJSONString(systemHook))
                .build();
        return covert(bo);
    }

    private static CsGitlabSystemHook covert(GitlabSystemHookBO bo) {
        return BeanCopierUtil.copyProperties(bo, CsGitlabSystemHook.class);
    }
}
