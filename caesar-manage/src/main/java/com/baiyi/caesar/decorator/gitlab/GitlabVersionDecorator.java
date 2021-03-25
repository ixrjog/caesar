package com.baiyi.caesar.decorator.gitlab;

import com.baiyi.caesar.domain.vo.gitlab.GitlabInstanceVO;
import com.baiyi.caesar.gitlab.handler.GitlabHandler;
import org.gitlab.api.models.GitlabVersion;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/3/17 4:55 下午
 * @Version 1.0
 */
@Component
public class GitlabVersionDecorator {

    @Resource
    private GitlabHandler gitlabHandler;

    public void decorator(GitlabInstanceVO.IVersion iVersion) {
        try {
            GitlabVersion gitlabVersion = gitlabHandler.getVersion(iVersion.getInstanceName());
            GitlabInstanceVO.Version version = GitlabInstanceVO.Version.builder()
                    .version(gitlabVersion.getVersion())
                    .revision(gitlabVersion.getRevision())
                    .build();
            iVersion.setVersion(version);
        } catch (IOException ignored) {
        }

    }
}
