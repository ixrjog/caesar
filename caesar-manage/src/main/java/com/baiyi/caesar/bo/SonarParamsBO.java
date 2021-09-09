package com.baiyi.caesar.bo;

import com.google.common.base.Joiner;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/12/23 4:23 下午
 * @Version 1.0
 */
@Data
public class SonarParamsBO {

    public static final String SONAR_BASE_URL = "https://sonar.jinweihaowu.com/api/project_badges/measure?";

    public SonarParamsBO() {
    }

    public SonarParamsBO(String projectKey, String metric) {
        this.projectKey = projectKey;
        this.metric = metric;
    }

    private String projectKey;

    private String metric;

    @Override
    public String toString() {
        return SONAR_BASE_URL + Joiner.on("=").join("project", projectKey) + "&" + Joiner.on("=").join("metric", metric);
    }

}
