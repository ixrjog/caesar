package com.baiyi.caesar.common.model;

import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/29 10:11 上午
 * @Version 1.0
 */
@Data
public class JenkinsJobParameters {

    private String version;
    private List<Parameter> parameters;

    @Data
    public static class Parameter{
        private String name;
        private String value;
        private String description;
    }

}
