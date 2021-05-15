package com.baiyi.caesar.packer.jenkins;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsInstanceVO;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/3/17 2:29 下午
 * @Version 1.0
 */
@Component
public class JenkinsVersionDecorator {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    public void wrap(JenkinsInstanceVO.IJenkinsVersion iJenkinsVersion) {
        JenkinsVersion jenkinsVersion = jenkinsServerHandler.getVersion(iJenkinsVersion.getInstanceName());
        if (jenkinsVersion != null)
            iJenkinsVersion.setVersion(jenkinsVersion.getLiteralVersion());
    }
}
