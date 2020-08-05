package com.baiyi.caesar.factory.jenkins.model;

import com.baiyi.caesar.common.model.JenkinsJobParameters;
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

    private JenkinsJobParameters jenkinsJobParameters;
    private Map<String, String> params;

}
