package com.baiyi.caesar.jenkins.context;

import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.baiyi.caesar.domain.generator.caesar.CsOssBucket;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/5 5:43 下午
 * @Version 1.0
 */
@Data
@Builder
public class JobParametersContext {

    @Builder.Default
    private String versionName = "";

    @Builder.Default
    private String versionDesc = "";

    @Builder.Default
    private Boolean isRollback = false;

    private CsOssBucket csOssBucket;

    private String jobName;

    private JenkinsJobParameters jenkinsJobParameters;

    private Map<String, String> params;

    public void putParam(String paramName, String paramValue) {
        this.params.put(paramName, paramValue);
    }

    public void putParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    public String getParamByKey(String key) {
        return params.getOrDefault(key, "");
    }

}
