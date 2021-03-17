package com.baiyi.caesar.decorator.gitlab;


import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.decorator.server.ServerDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabInstance;
import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.service.gitlab.CsGitlabInstanceService;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/20 8:47 上午
 * @Version 1.0
 */
@Component
public class GitlabInstanceDecorator {

    @Resource
    private CsGitlabProjectService csGitlabProjectService;

    @Resource
    private CsGitlabInstanceService csGitlabInstanceService;

    @Resource
    private ServerDecorator serverDecorator;

    @Resource
    private  GitlabVersionDecorator gitlabVersionDecorator;

    public void decorator(GitlabInstanceVO.IInstance iInstance){
        CsGitlabInstance csGitlabInstance = csGitlabInstanceService.queryCsGitlabInstanceById(iInstance.getInstanceId());
        iInstance.setInstance(BeanCopierUtil.copyProperties(csGitlabInstance, GitlabInstanceVO.Instance.class));
    }

    public GitlabInstanceVO.Instance decorator(GitlabInstanceVO.Instance instance, Integer extend) {
        instance.setToken("");
        if (extend == 0) return instance;
        serverDecorator.decorator(instance);
        gitlabVersionDecorator.decorator(instance); // 版本
        // 项目数量
        instance.setProjectSize(csGitlabProjectService.countCsGitlabProjectByInstanceId(instance.getId()));
        return instance;
    }
}
