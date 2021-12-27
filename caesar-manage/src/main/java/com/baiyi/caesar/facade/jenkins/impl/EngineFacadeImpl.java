package com.baiyi.caesar.facade.jenkins.impl;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.tree.EngineVO;
import com.baiyi.caesar.facade.jenkins.EngineFacade;
import com.baiyi.caesar.factory.jenkins.model.JobBuild;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.util.JobBuildUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/11/18 10:58 上午
 * @Version 1.0
 */
@Slf4j
@Component
public class EngineFacadeImpl implements EngineFacade {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    public final static String ENGINES = "Engines";

    public final static String INACTIVE = "(inactive)";

    public final static String MASTER = "built-in node";

    public final static String IDLE = "idle";

    public static EngineVO.Children chart;

    @Override
    @Cacheable(cacheNames = CachingConfig.CacheRepositories.ENGINE_CHART)
    public EngineVO.Children buildEngineChart() {
        List<EngineVO.Children> instances = buildInstances();
        EngineVO.Children chart = EngineVO.Children.builder()
                .name(ENGINES)
                .children(instances)
                .value(instances.size())
                .build();
        EngineFacadeImpl.chart = chart;
        return chart;
    }

    private List<EngineVO.Children> buildInstances() {
        return csJenkinsInstanceService.queryAll().stream().map(i -> {
            if (i.getIsActive()) {
                List<EngineVO.Children> computers = buildComputers(i);
                return EngineVO.Children.builder()
                        .name(buildEngineName(i, i.getIsActive()))
                        .children(computers)
                        .value(computers.size())
                        .build();
            } else {
                return EngineVO.Children.builder()
                        .name(buildEngineName(i, i.getIsActive()))
                        .build();
            }
        }).collect(Collectors.toList());
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

    private List<EngineVO.Children> buildComputers(CsJenkinsInstance csJenkinsInstance) {
        List<EngineVO.Children> computers = Lists.newArrayList();
        try {
            Map<String, Computer> computerMap = jenkinsServerHandler.getComputerMap(csJenkinsInstance.getName());
            computerMap.keySet().forEach(k -> {
                if (!k.equals(MASTER)) {
                    List<EngineVO.Children> executors = buildExecutors(computerMap.get(k));
                    Computer computer = computerMap.get(k);
                    String name = k;
                    try {
                        if (computer.details().getOffline())
                            name += "(offline)";
                    } catch (IOException e) {
                        log.error("查询节点状态错误!, name = {}, err= {} ", k, e.getMessage());
                    }
                    EngineVO.Children node = EngineVO.Children.builder()
                            .name(name)
                            .children(executors)
                            .value(executors.size())
                            .build();
                    computers.add(node);
                }
            });
        } catch (Exception e) {
            log.error("组装Jenkins引擎工作负载错误!, err={}", e.getMessage());
        }
        return computers;
    }

    private List<EngineVO.Children> buildExecutors(Computer computer) {
        List<EngineVO.Children> executors = Lists.newArrayList();
        try {
            ComputerWithDetails computerWithDetails = computer.details();
            computerWithDetails.getExecutors().forEach(e -> {
                EngineVO.Children executor = EngineVO.Children.builder()
                        .name(acqExecutorName(e))
                        .value(1)
                        .build();
                executors.add(executor);
            });
        } catch (IOException ignored) {
        }
        return executors;
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
