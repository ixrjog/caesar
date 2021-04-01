package com.baiyi.caesar.jenkins.handler;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.jenkins.api.clinet.JenkinsClient;
import com.baiyi.caesar.jenkins.api.mapper.PipelineNodeMapper;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/23 1:50 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class JenkinsBlueHandler {

    @Resource
    private JenkinsClient jenkinsClient;

    public interface BlueURL {
        String BASE = "blue/rest/organizations/jenkins";
        String RUNS = "runs";
        String NODES = "nodes";
        String PIPLINES = "pipelines";
    }

    public List<PipelineNode> queryJobRunNodes(String serverName, String jobName, Integer buildNumber) {
        // https://cc1.xinc818.com/blue/rest/organizations/jenkins/pipelines/ACCOUNT_account-server-build-prod/runs/1/nodes/?limit=10000
        String api = Joiner.on("/").join(BlueURL.BASE, BlueURL.PIPLINES, jobName, BlueURL.RUNS, buildNumber, BlueURL.NODES);
        try {
            JsonNode jn = jenkinsClient.get(serverName, api);
            return new PipelineNodeMapper().mapFromJson(jn);
        } catch (IOException e) {
            log.error("查询JobRunNodes错误! serverName = {}, jobName = {} , buildNumber = {}", serverName, jobName, buildNumber);
        }
        return null;
    }

    @Cacheable(cacheNames = CachingConfig.CacheRepositories.JOB_RUN_NODES,unless="#result == null")
    public List<PipelineNode> queryJobRunNodesByCache(String serverName, String jobName, Integer buildNumber){
       return queryJobRunNodes(serverName,jobName,buildNumber);
    }


}
