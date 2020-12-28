package com.baiyi.caesar.sonar.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @Author baiyi
 * @Date 2020/12/24 4:37 下午
 * @Version 1.0
 */
public interface SonarMapper<T> {

    T mapFromJson(JsonNode jsonNode) throws JsonProcessingException;

}
