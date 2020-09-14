package com.baiyi.caesar.builder.jenkins;

import com.baiyi.caesar.bo.jenkins.JobBuildServerBO;
import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildServer;
import com.baiyi.caesar.domain.generator.caesar.OcServer;
import com.baiyi.caesar.jenkins.context.DeploymentJobContext;

/**
 * @Author baiyi
 * @Date 2020/9/11 5:50 下午
 * @Version 1.0
 */
public class JobBuildServerBuilder {

    public static CsJobBuildServer build(DeploymentJobContext context, String hostPattern, OcServer server) {
        JobBuildServerBO bo = JobBuildServerBO.builder()
                .buildType(BuildType.DEPLOYMENT.getType())
                .buildId(context.getJobBuild().getId())
                .jobId(context.getCsCdJob().getId())
                .hostPattern(hostPattern)
                .versionName(context.getJobParamDetail().getVersionName())
                .serverName(server.getName())
                .serialNumber(server.getSerialNumber())
                .envType(server.getEnvType())
                .privateIp(server.getPrivateIp())
                .serverId(server.getId())
                .build();
        return covert(bo);
    }

    private static CsJobBuildServer covert(JobBuildServerBO bo) {
        return BeanCopierUtils.copyProperties(bo, CsJobBuildServer.class);
    }
}
