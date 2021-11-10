package com.baiyi.caesar.sonar.param;

import com.baiyi.caesar.common.util.JSONUtil;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/3/15 10:42 上午
 * @Version 1.0
 */
@Data
public class SonarQubeParamMap {

    private Map<String, String> params = Maps.newHashMap();

    public void putParam(String key, String value) {
        this.params.put(key, value);
    }

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }
}
