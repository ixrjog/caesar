package com.baiyi.caesar.common.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.caesar.common.model.JenkinsJobParameters;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/29 10:16 上午
 * @Version 1.0
 */
public class JenkinsUtils {

    public static JenkinsJobParameters convert(String parametersYaml) throws JsonSyntaxException {
        Yaml yaml = new Yaml();
        Object result = yaml.load(parametersYaml);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(JSON.toJSONString(result), JenkinsJobParameters.class);
    }

    public static Map<String, String> convert(JenkinsJobParameters jenkinsJobParameters) {
        Map<String, String> jobParameterMap = Maps.newHashMap();
        if (jenkinsJobParameters == null || CollectionUtils.isEmpty(jenkinsJobParameters.getParameters()))
            return jobParameterMap;
        return jenkinsJobParameters.getParameters()
                .stream().collect(Collectors.toMap(JenkinsJobParameters.Parameter::getName, JenkinsJobParameters.Parameter::getValue, (k1, k2) -> k1));

    }

}
