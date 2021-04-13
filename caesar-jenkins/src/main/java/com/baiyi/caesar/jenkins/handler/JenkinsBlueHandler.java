package com.baiyi.caesar.jenkins.handler;

import com.baiyi.caesar.common.config.CachingConfig;
import com.baiyi.caesar.jenkins.api.clinet.JenkinsClient;
import com.baiyi.caesar.jenkins.api.mapper.PipelineNodeMapper;
import com.baiyi.caesar.jenkins.api.mapper.PipelineStepMapper;
import com.baiyi.caesar.jenkins.api.model.PipelineNode;
import com.baiyi.caesar.jenkins.api.model.PipelineStep;
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
        String STEPS = "steps";
        String LOG = "log";
    }

    public List<PipelineStep> queryJobRunNodesSteps(String serverName, String jobName, Integer buildNumber, Integer nodeId) {
        // /blue/rest/organizations/jenkins/pipelines/OUTWAY-DOWNLOAD_outway-download-server-dev/runs/16/nodes/21/steps/
        String api = Joiner.on("/").join(BlueURL.BASE, BlueURL.PIPLINES, jobName, BlueURL.RUNS, buildNumber, BlueURL.NODES, nodeId, BlueURL.STEPS);
        try {
            JsonNode jn = jenkinsClient.get(serverName, api);
            return new PipelineStepMapper().mapFromJson(jn);
        } catch (IOException e) {
            log.error("查询JobRunNodes错误! serverName = {}, jobName = {} , buildNumber = {}", serverName, jobName, buildNumber);
        }
        return null;
    }

    public String queryJobRunNodesStepLog(String serverName, String jobName, Integer buildNumber, Integer nodeId,String stepId) {
        // /blue/rest/organizations/jenkins/pipelines/OUTWAY-DOWNLOAD_outway-download-server-dev/runs/17/nodes/21/steps/26/log/
        String api = Joiner.on("/").join(BlueURL.BASE, BlueURL.PIPLINES, jobName, BlueURL.RUNS, buildNumber, BlueURL.NODES, nodeId, BlueURL.STEPS,stepId,BlueURL.LOG);
        try {
            return jenkinsClient.getString(serverName, api);
        } catch (IOException e) {
            log.error("查询JobRunNodes错误! serverName = {}, jobName = {} , buildNumber = {}", serverName, jobName, buildNumber);
        }
        return null;
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

    @Cacheable(cacheNames = CachingConfig.CacheRepositories.JOB_RUN_NODES, unless = "#result == null")
    public List<PipelineNode> queryJobRunNodesByCache(String serverName, String jobName, Integer buildNumber) {
        return queryJobRunNodes(serverName, jobName, buildNumber);
    }


}
