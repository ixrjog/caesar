package com.baiyi.caesar.service.gitlab;

import com.baiyi.caesar.domain.generator.caesar.CsGitlabSystemHook;

/**
 * @Author baiyi
 * @Date 2020/12/22 1:51 下午
 * @Version 1.0
 */
public interface CsGitlabSystemHookService {

    void addCsGitlabSystemHook(CsGitlabSystemHook csGitlabSystemHook);

    void updateCsGitlabSystemHook(CsGitlabSystemHook csGitlabSystemHook);
}
