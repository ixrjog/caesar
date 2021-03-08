package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.ApplicationScmMemberBO;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationScmMember;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;

/**
 * @Author baiyi
 * @Date 2020/7/22 4:25 下午
 * @Version 1.0
 */
public class ApplicationScmMemberBuilder {

    public static CsApplicationScmMember build(int applicationId, CsGitlabProject csGitlabProject) {
        ApplicationScmMemberBO bo = ApplicationScmMemberBO.builder()
                .applicationId(applicationId)
                .scmType("GITLAB")
                .scmId(csGitlabProject.getId())
                .scmSshUrl(csGitlabProject.getSshUrl())
                .comment(csGitlabProject.getDescription())
                .build();
        return covert(bo);
    }

    private static CsApplicationScmMember covert(ApplicationScmMemberBO bo) {
        return BeanCopierUtil.copyProperties(bo, CsApplicationScmMember.class);
    }
}
