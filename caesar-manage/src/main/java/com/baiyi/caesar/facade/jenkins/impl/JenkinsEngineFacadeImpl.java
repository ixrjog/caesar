package com.baiyi.caesar.facade.jenkins.impl;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.tree.EngineVO;
import com.baiyi.caesar.facade.jenkins.JenkinsEngineFacade;
import com.baiyi.caesar.factory.jenkins.model.JobBuild;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.util.JobBuildUtils;
import com.google.common.base.Joiner;
import com.offbytwo.jenkins.helper.JenkinsVersion;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.ComputerWithDetails;
import com.offbytwo.jenkins.model.Executor;
import com.offbytwo.jenkins.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/11/18 10:58 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsEngineFacadeImpl implements JenkinsEngineFacade {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    public final static String ENGINES = "Engines";

    public final static String INACTIVE = "(inactive)";

    public final static String MASTER = "master";

    public final static String IDLE = "idle";

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.ENGINE_CHART)
    public EngineVO.Children buildEngineChart() {
        EngineVO.Children root = EngineVO.Children.builder()
                .name(ENGINES)
                .build();
        csJenkinsInstanceService.queryAll().forEach(e -> {
            EngineVO.Children instance = EngineVO.Children.builder()
                    .name(buildEngineName(e, e.getIsActive()))
                    .build();
            if (e.getIsActive()) {
                assembleComputer(e, instance);
                instance.setValue(instance.getChildren().size());
            }
            root.addChildren(instance);
        });
        root.setValue(root.getChildren().size());
        return root;
    }

    /**
     * 取引擎名称
     *
     * @param instance
     * @param isActive
     * @return
     */
    private String buildEngineName(CsJenkinsInstance instance, boolean isActive) {
        return instance.getName() + acqEngineName(instance, isActive);
    }

    private String acqEngineName(CsJenkinsInstance instance, boolean isActive) {
        if (isActive) {
            JenkinsVersion version = jenkinsServerHandler.getVersion(instance.getName());
            return Joiner.on("").join("(", version.getLiteralVersion(), ")");
        } else {
            return INACTIVE;
        }
    }

    private void assembleComputer(CsJenkinsInstance csJenkinsInstance, EngineVO.Children instance) {
        try {
            Map<String, Computer> computerMap = jenkinsServerHandler.getComputerMap(csJenkinsInstance.getName());
            computerMap.keySet().forEach(k -> {
                if (!k.equals(MASTER)) {
                    EngineVO.Children node = EngineVO.Children.builder()
                            .name(k)
                            .build();
                    Computer computer = computerMap.get(k);
                    try {
                        ComputerWithDetails computerWithDetails = computer.details();
                        computerWithDetails.getExecutors().forEach(e -> {
                            EngineVO.Children executor = EngineVO.Children.builder()
                                    .name(acqExecutorName(e))
                                    .value(1)
                                    .build();
                            node.addChildren(executor);
                        });
                        node.setValue(node.getChildren().size());
                        instance.addChildren(node);
                    } catch (IOException ignored) {
                    }
                }
            });
        } catch (Exception e) {
            log.error("组装Jenkins引擎工作负载错误!, err={}", e.getMessage());
        }
    }

    private String acqExecutorName(Executor executor) {
        if (executor.getCurrentExecutable() != null) {
            Job job = executor.getCurrentExecutable();
            JobBuild jobBuild = JobBuildUtils.convert(job.getUrl());
            return jobBuild.getJobName();
        } else {
            return IDLE;
        }
    }

}
