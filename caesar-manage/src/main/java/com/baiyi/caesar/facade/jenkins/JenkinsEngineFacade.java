package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.vo.tree.EngineVO;
import com.baiyi.caesar.factory.jenkins.model.JobBuild;
import com.baiyi.caesar.jenkins.handler.JenkinsServerHandler;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.baiyi.caesar.util.JobBuildUtils;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.ComputerWithDetails;
import com.offbytwo.jenkins.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/31 5:46 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsEngineFacade {

    @Resource
    private JenkinsServerHandler jenkinsServerHandler;

    @Resource
    private CsJenkinsInstanceService csJenkinsInstanceService;

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_ENGINE_CHART_CACHE_REPO)
    public EngineVO.Children buildEngineChart() {
        EngineVO.Children root = EngineVO.Children.builder()
                .name("Engines")
                .build();
        csJenkinsInstanceService.queryAll().forEach(e -> {
            EngineVO.Children instance;
            if (e.getIsActive()) {
                instance = EngineVO.Children.builder()
                        .name(e.getName())
                        .build();
                invokeComputer(e, instance);
                instance.setValue(instance.getChildren().size());
            } else {
                instance = EngineVO.Children.builder()
                        .name(e.getName() + "(inactive)")
                        .build();
            }
            root.addChildren(instance);
        });
        root.setValue(root.getChildren().size());
        return root;
    }

    private void invokeComputer(CsJenkinsInstance csJenkinsInstance, EngineVO.Children instance) {
        try {
            Map<String, Computer> computerMap = jenkinsServerHandler.getComputerMap(csJenkinsInstance.getName());
            computerMap.keySet().forEach(k -> {
                if (!k.equals("master")) {
                    EngineVO.Children node = EngineVO.Children.builder()
                            .name(k)
                            .build();
                    Computer computer = computerMap.get(k);
                    try {
                        ComputerWithDetails computerWithDetails = computer.details();
                        computerWithDetails.getExecutors().forEach(e -> {
                            EngineVO.Children executor;
                            if (e.getCurrentExecutable() != null) {
                                Job job = e.getCurrentExecutable();
                                JobBuild jobBuild = JobBuildUtils.convert(job.getUrl());
                                executor = EngineVO.Children.builder()
                                        .name(jobBuild.getJobName())
                                        .value(1)
                                        .build();
                            }else{
                                executor = EngineVO.Children.builder()
                                        .name("idle")
                                        .value(1)
                                        .build();
                            }
                            node.addChildren(executor);
                        });
                        node.setValue(node.getChildren().size());
                        instance.addChildren(node);
                    } catch (IOException ignored) {
                    }
                }
            });
        } catch (Exception e) {
            log.error("组装Jenkins引擎工作负载错误, err={}",e.getMessage());
        }
    }

}
