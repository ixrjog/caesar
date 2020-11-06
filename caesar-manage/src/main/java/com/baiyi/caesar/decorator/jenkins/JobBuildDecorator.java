package com.baiyi.caesar.decorator.jenkins;

import com.baiyi.caesar.common.base.BuildType;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.TimeAgoUtils;
import com.baiyi.caesar.common.util.TimeUtils;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.domain.vo.build.BuildExecutorVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.jenkins.*;
import com.baiyi.caesar.service.server.OcServerService;
import com.baiyi.caesar.service.user.OcUserService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/8/10 10:13 上午
 * @Version 1.0
 */
@Component
public class JobBuildDecorator {

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JobEngineDecorator jobEngineDecorator;

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    private JobBuildArtifactDecorator jobBuildArtifactDecorator;

    @Resource
    private CsCiJobService csCiJobService;

    @Resource
    private CsOssBucketService ossBucketService;

    @Resource
    private CsJobBuildChangeService csJobBuildChangeService;

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcUserService ocUserService;

    public CiJobBuildVO.JobBuild decorator(CiJobBuildVO.JobBuild jobBuild, Integer extend) {
        if (extend == 1) {
            // 装饰工作引擎
            CsJobEngine csCiJobEngine = csJobEngineService.queryCsJobEngineById(jobBuild.getJobEngineId());
            if (csCiJobEngine != null) {
                JobEngineVO.JobEngine jobEngine = jobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobEngine, JobEngineVO.JobEngine.class));
                jobBuild.setJobEngine(jobEngine);

                String jobBuildUrl = Joiner.on("/").join(jobEngine.getJobUrl(), jobBuild.getEngineBuildNumber());
                jobBuild.setJobBuildUrl(jobBuildUrl);
            }

            CsCiJob csCiJob = csCiJobService.queryCsCiJobById(jobBuild.getCiJobId());
            CsOssBucket csOssBucket = ossBucketService.queryCsOssBucketById(csCiJob.getOssBucketId());

            List<CsJobBuildArtifact> artifacts = csJobBuildArtifactService.queryCsJobBuildArtifactByBuildId(BuildType.BUILD.getType(), jobBuild.getId());
            if (CollectionUtils.isEmpty(artifacts)) {
                jobBuild.setNoArtifact(true);
            } else {
                jobBuild.setArtifacts(jobBuildArtifactDecorator.decorator(artifacts, csOssBucket));
                jobBuild.setNoArtifact(false);
            }

            jobBuild.setChanges(getBuildChangeByBuildId(jobBuild.getId()));

            if (!StringUtils.isEmpty(jobBuild.getUsername())) {
                OcUser ocUser = ocUserService.queryOcUserByUsername(jobBuild.getUsername());
                if (ocUser != null) {
                    ocUser.setPassword("");
                    jobBuild.setUser(BeanCopierUtils.copyProperties(ocUser, UserVO.User.class));
                }
            }
            if (jobBuild.getStartTime() != null && jobBuild.getEndTime() != null) {
                long buildTime = jobBuild.getEndTime().getTime() - jobBuild.getStartTime().getTime();
                jobBuild.setBuildTime(TimeUtils.acqBuildTime(buildTime));
            }

            jobBuild.setExecutors(getBuildExecutorByBuildId(jobBuild.getId()));
        }
        // Ago
        jobBuild.setAgo(TimeAgoUtils.format(jobBuild.getStartTime()));

        return jobBuild;
    }

    private List<CiJobBuildVO.BuildChange> getBuildChangeByBuildId(int buildId) {
        List<CsJobBuildChange> changes = csJobBuildChangeService.queryCsJobBuildChangeByBuildId(BuildType.BUILD.getType(), buildId);
        return changes.stream().map(e -> {
            CiJobBuildVO.BuildChange buildChange = BeanCopierUtils.copyProperties(e, CiJobBuildVO.BuildChange.class);
            buildChange.setShortCommitId(buildChange.getCommitId().substring(0, 7));
            return buildChange;
        }).collect(Collectors.toList());
    }

    public List<BuildExecutorVO.BuildExecutor> getBuildExecutorByBuildId(int buildId) {
        List<CsJobBuildExecutor> executors = csJobBuildExecutorService.queryCsJobBuildExecutorByBuildId(BuildType.BUILD.getType(), buildId);
        return executors.stream().map(e -> {
                    BuildExecutorVO.BuildExecutor buildExecutor = BeanCopierUtils.copyProperties(e, BuildExecutorVO.BuildExecutor.class);
                    OcServer ocServer = ocServerService.queryOcServerByIp(buildExecutor.getPrivateIp());
                    if (ocServer != null)
                        buildExecutor.setServer(BeanCopierUtils.copyProperties(ocServer, ServerVO.Server.class));
                    return buildExecutor;
                }
        ).collect(Collectors.toList());
    }

}
