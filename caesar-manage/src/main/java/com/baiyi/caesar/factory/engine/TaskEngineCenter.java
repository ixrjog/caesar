package com.baiyi.caesar.factory.engine;

import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.jenkins.context.BuildJobContext;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;

/**
 * @Author baiyi
 * @Date 2020/8/5 10:30 上午
 * @Version 1.0
 */

public interface TaskEngineCenter {

    JobEngineVO.JobEngine acqJobEngineByJobEngineId(int jobEngineId);

    boolean tryJenkinsInstanceActive(int jenkinsInstanceId);

    void trackBuildTask(BuildJobContext context);

    void trackBuildTask(DeploymentJobContext context);
}
