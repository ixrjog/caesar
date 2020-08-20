package com.baiyi.caesar.factory.jenkins.model;

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
public class JobParamDetail {

    private String versionName;
    private String versionDesc;

    private CsOssBucket csOssBucket;
    private String jobName;

    private JenkinsJobParameters jenkinsJobParameters;
    private Map<String, String> params;


    public String getParamByKey(String key){
       return params.getOrDefault(key,"");
    }

}
