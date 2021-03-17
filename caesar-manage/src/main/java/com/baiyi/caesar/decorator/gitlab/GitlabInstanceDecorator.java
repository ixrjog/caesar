package com.baiyi.caesar.decorator.gitlab;


import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.gitlab.handler.GitlabHandler;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.baiyi.caesar.service.server.OcServerService;
import org.gitlab.api.models.GitlabVersion;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:47 上午
 * @Version 1.0
 */
@Component
public class GitlabInstanceDecorator {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private GitlabHandler gitlabHandler;

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private CsGitlabInstanceService csGitlabInstanceService;

    public void decorator(GitlabInstanceVO.IInstance iInstance){
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(iInstance.getInstanceId());
        iInstance.setInstance(BeanCopierUtil.copyProperties(csGitlabInstance, GitlabInstanceVO.Instance.class));
    }

    public GitlabInstanceVO.Instance decorator(GitlabInstanceVO.Instance instance, Integer extend) {
        if (extend == 0) return instance;

        instance.setToken("");
        if (!IDUtil.isEmpty(instance.getServerId())) {
            OcServer ocServer = ocServerService.queryOcServerById(instance.getServerId());
            if (ocServer != null)
                instance.setServer(BeanCopierUtil.copyProperties(ocServer, ServerVO.Server.class));
        }
        try {
            GitlabVersion gitlabVersion = gitlabHandler.getVersion(instance.getName());
            GitlabInstanceVO.Version version = new GitlabInstanceVO.Version();
            version.setVersion(gitlabVersion.getVersion());
            version.setRevision(gitlabVersion.getRevision());
            instance.setVersion(version);
        } catch (IOException ignored) {
        }
        // 项目数量
        instance.setProjectSize(csGitlabProjectService.countCsGitlabProjectByInstanceId(instance.getId()));
        return instance;
    }
}
