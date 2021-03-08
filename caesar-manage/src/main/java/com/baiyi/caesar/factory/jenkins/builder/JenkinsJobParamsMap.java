package com.baiyi.caesar.factory.jenkins.builder;

import com.baiyi.caesar.common.util.JSONUtil;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/1/12 5:32 下午
 * @Version 1.0
 */
@Data
public class JenkinsJobParamsMap {

    private Map<String, String> params = Maps.newHashMap();

    public void putParam(String key, String value) {
        this.params.put(key, value);
    }

    public void putParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }
}
