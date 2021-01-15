package com.baiyi.caesar.factory.jenkins.builder;

import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import com.baiyi.caesar.domain.param.jenkins.JobDeploymentParam;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/1/12 5:30 下午
 * @Version 1.0
 */
public class JenkinsJobParamsBuilder {

    private JenkinsJobParamsMap paramsMap = new JenkinsJobParamsMap();

    private JenkinsJobParamsBuilder() {
    }

    static public JenkinsJobParamsBuilder newBuilder() {
        return new JenkinsJobParamsBuilder();
    }

    public JenkinsJobParamsBuilder paramEntry(String paramName, JobBuildParam.BuildParam buildParam) {
        if (buildParam.getParamMap().containsKey(paramName))
            paramsMap.putParam(paramName, buildParam.getParamMap().get(paramName));
        return this;
    }

    public JenkinsJobParamsBuilder paramEntry(String paramName, JobDeploymentParam.DeploymentParam deploymentParam) {
        if (deploymentParam.getParamMap().containsKey(paramName))
            paramsMap.putParam(paramName, deploymentParam.getParamMap().get(paramName));
        return this;
    }

    public JenkinsJobParamsBuilder paramEntry(String paramName, String value) {
        if (!StringUtils.isEmpty(value))
            paramsMap.putParam(paramName, value);
        return this;
    }

    public JenkinsJobParamsBuilder paramEntries(Map<String, String> entries) {
        paramsMap.putParams(entries);
        return this;
    }

    public JenkinsJobParamsMap build() {
        return paramsMap;
    }
}
