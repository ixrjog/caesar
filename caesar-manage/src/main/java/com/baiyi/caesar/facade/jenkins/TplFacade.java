package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.vo.jenkins.JenkinsJobVO;

import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/23 2:16 下午
 * @Version 1.0
 */

public interface TplFacade {

    String getJobContent(int instanceId, String jobName) throws IOException;

    void updateJobContent(int instanceId, String jobName, String jobXml) throws IOException;

    List<JenkinsJobVO.Job> queryJenkinsInstanceTpl(int instanceId);

}

