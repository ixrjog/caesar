package com.baiyi.caesar.sonar.param;

/**
 * @Author baiyi
 * @Date 2021/3/15 10:45 上午
 * @Version 1.0
 */
public class SonarQubeRequestBuilder {

    private SonarQubeParamMap request = new SonarQubeParamMap();

    private SonarQubeRequestBuilder() {
    }

    static public SonarQubeRequestBuilder newBuilder() {
        return new SonarQubeRequestBuilder();
    }

    public SonarQubeParamMap build() {
        return request;
    }

    public SonarQubeRequestBuilder paramEntry(String key, String value) {
        request.putParam(key, value);
        return this;
    }
}
