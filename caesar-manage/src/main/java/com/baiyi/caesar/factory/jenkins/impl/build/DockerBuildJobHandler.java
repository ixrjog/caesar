package com.baiyi.caesar.factory.jenkins.impl.build;

import com.baiyi.caesar.common.type.JobTypeEnum;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.generator.caesar.CsJobBuildArtifact;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.factory.jenkins.IBuildJobHandler;
import com.baiyi.caesar.jenkins.context.JobParametersContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.caesar.common.base.Build.ROLLBACK_JOB_BUILD_ID;
import static com.baiyi.caesar.common.base.Global.BRANCH;

/**
 * @Author baiyi
 * @Date 2021/6/2 1:57 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class DockerBuildJobHandler extends BaseBuildJobHandler implements IBuildJobHandler {

    @Override
    public String getKey() {
        return JobTypeEnum.DOCKER.getType();
    }

    @Override
    protected JobParametersContext buildJobParametersContext(CsCiJob csCiJob, JobBuildParam.BuildParam buildParam) {
        JobParametersContext context = super.buildJobParametersContext(csCiJob, buildParam);
        if (isRollback(buildParam)) {
            CsCiJobBuild csCiJobBuild = queryCiJobBuildById(Integer.parseInt(buildParam.getParamMap().get(ROLLBACK_JOB_BUILD_ID)));
            context.putParam(BRANCH, csCiJobBuild.getCommit()); // 使用回滚任务的Commit
            context.setVersionName(csCiJobBuild.getVersionName());
            context.setVersionDesc(csCiJobBuild.getVersionDesc());
        }
        return context;
    }

    @Override
    public String acqOssPath(CiJobBuildVO.JobBuild jobBuild, CsJobBuildArtifact csJobBuildArtifact) {
        return "";
    }

}
