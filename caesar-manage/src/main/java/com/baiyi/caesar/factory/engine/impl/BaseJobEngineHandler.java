package com.baiyi.caesar.factory.engine.impl;

import com.aliyun.oss.model.OSSObjectSummary;
import com.baiyi.caesar.aliyun.oss.handler.AliyunOSSHandler;
import com.baiyi.caesar.common.redis.RedisUtil;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.decorator.application.JobEngineDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.vo.application.JobEngineVO;
import com.baiyi.caesar.facade.ServerBaseFacade;
import com.baiyi.caesar.facade.jenkins.JenkinsJobFacade;
import com.baiyi.caesar.factory.engine.IJobEngineHandler;
import com.baiyi.caesar.factory.engine.JobEngineHandlerFactory;
import com.baiyi.caesar.jenkins.context.BaseJobContext;
import com.baiyi.caesar.jenkins.handler.JenkinsJobHandler;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.aliyun.CsOssBucketService;
import com.baiyi.caesar.service.jenkins.*;
import com.baiyi.caesar.service.server.OcServerService;
import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.JobWithDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/11/20 9:52 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseJobEngineHandler<T extends BaseJobContext> implements IJobEngineHandler<T>, InitializingBean {

    @Resource
    protected RedisUtil redisUtil;

    @Resource
    private CsJobEngineService csJobEngineService;

    @Resource
    private JenkinsJobFacade jenkinsJobFacade;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Resource
    protected JenkinsServerHandler jenkinsServerHandler;

    @Resource
    protected JenkinsJobHandler jenkinsJobHandler;

    @Resource
    private JobEngineDecorator jobEngineDecorator;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private CsJobBuildExecutorService csJobBuildExecutorService;

    @Resource
    private CsJobBuildArtifactService csJobBuildArtifactService;

    @Resource
    protected CsOssBucketService csOssBucketService;

    @Resource
    protected AliyunOSSHandler aliyunOSSHandler;

    @Resource
    protected CsJobBuildChangeService csJobBuildChangeService;

    public static final int TRACK_SLEEP_SECONDS = 5;

    @Override
    public BusinessWrapper<JobEngineVO.JobEngine> acqJobEngine(int jobId) {
        return acqJobEngineByJobEngines(queryJobEngine(jobId));
    }

    private BusinessWrapper<JobEngineVO.JobEngine> acqJobEngineByJobEngines(List<CsJobEngine> csJobEngines) {
        if (CollectionUtils.isEmpty(csJobEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_ENGINE_NOT_CONFIGURED); // 工作引擎未配置
        List<CsJobEngine> activeEngines = csJobEngines.stream().filter(e ->
                tryJenkinsInstanceActive(e.getJenkinsInstanceId())
        ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(activeEngines))
            return new BusinessWrapper<>(ErrorEnum.JENKINS_JOB_NO_ENGINES_AVAILABLE); // 无可用工作引擎
        return new BusinessWrapper<>(buildRandomCiJobEngine(activeEngines));
    }

    private JobEngineVO.JobEngine buildRandomCiJobEngine(List<CsJobEngine> activeEngines) {
        Random random = new Random();
        int n = random.nextInt(activeEngines.size());
        CsJobEngine csCiJobEngine = activeEngines.get(n);
        return jobEngineDecorator.decorator(BeanCopierUtils.copyProperties(csCiJobEngine, JobEngineVO.JobEngine.class));
    }

    public boolean tryJenkinsInstanceActive(int jenkinsInstanceId) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jenkinsInstanceId);
        if (csJenkinsInstance == null) return false;
        if (!csJenkinsInstance.getIsActive())
            return false;
        // 校验实例是否正常
        return jenkinsServerHandler.isActive(csJenkinsInstance.getName());
    }

    @Override
    public List<CsJobEngine> queryJobEngine(int jobId) {
        List<CsJobEngine> csJobEngines = csJobEngineService.queryCsJobEngineByJobId(getKey(), jobId);
        if (CollectionUtils.isEmpty(csJobEngines))
            jenkinsJobFacade.createJobEngine(getKey(), jobId);
        return csJobEngineService.queryCsJobEngineByJobId(getKey(), jobId);
    }

    protected void recordJobEngine(JobWithDetails job, JobEngineVO.JobEngine jobEngine) {
        if (job.getNextBuildNumber() == jobEngine.getNextBuildNumber()) return;
        jobEngine.setNextBuildNumber(job.getNextBuildNumber());
        jobEngine.setLastBuildNumber(job.getNextBuildNumber() - 1);
        csJobEngineService.updateCsJobEngine(BeanCopierUtils.copyProperties(jobEngine, CsJobEngine.class));
    }

    protected void recordJobBuildComputer(JobEngineVO.JobEngine jobEngine, CsJobBuildExecutor pre) {
        CsJenkinsInstance csJenkinsInstance = csJenkinsInstanceService.queryCsJenkinsInstanceById(jobEngine.getJenkinsInstanceId());
        List<OcServer> nodeList = ocServerService.queryOcServerByServerGroupId(csJenkinsInstance.getNodeServerGroupId());
        for (OcServer ocServer : nodeList) {
            String nodeName = ServerBaseFacade.acqServerName(ocServer);
            if (nodeName.equals(pre.getNodeName())) {
                pre.setPrivateIp(ocServer.getPrivateIp());
                break;
            }
        }
        csJobBuildExecutorService.addCsJobBuildExecutor(pre);
    }

    protected void saveJobBuildArtifact(T context, Artifact artifact) {
        CsJobBuildArtifact pre = buildJobBuildArtifact(context, artifact);
        if (csJobBuildArtifactService.queryCsJobBuildArtifactByUniqueKey(getKey(), pre.getBuildId(), pre.getArtifactFileName()) == null)
            csJobBuildArtifactService.addCsJobBuildArtifact(pre);
    }

    protected abstract CsJobBuildArtifact acqJobBuildArtifact(T context, Artifact artifact);

    protected abstract CsOssBucket acqOssBucket(T context);

    protected abstract String acqOssPath(T context, CsJobBuildArtifact csJobBuildArtifact);

    protected CsJobBuildArtifact buildJobBuildArtifact(T context, Artifact artifact) {
        CsJobBuildArtifact csJobBuildArtifact = acqJobBuildArtifact(context, artifact);
        CsOssBucket ossBucket = acqOssBucket(context);
        String ossPath = acqOssPath(context, csJobBuildArtifact);
        csJobBuildArtifact.setStoragePath(ossPath);
        List<OSSObjectSummary> objects = aliyunOSSHandler.listObjects(ossBucket.getName(), ossPath);
        if (!CollectionUtils.isEmpty(objects)) {
            OSSObjectSummary ossObjectSummary = objects.get(0);
            csJobBuildArtifact.setStoragePath(ossObjectSummary.getKey());
            csJobBuildArtifact.setArtifactSize(ossObjectSummary.getSize());
        }
        return csJobBuildArtifact;
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        JobEngineHandlerFactory.register(this);
    }
}
