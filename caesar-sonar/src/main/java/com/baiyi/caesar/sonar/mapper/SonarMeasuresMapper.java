package com.baiyi.caesar.sonar.mapper;

import com.baiyi.caesar.domain.vo.sonar.SonarMeasures;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author baiyi
 * @Date 2020/12/24 4:37 下午
 * @Version 1.0
 */
public class SonarMeasuresMapper implements SonarMapper<SonarMeasures> {


    @Override
    public SonarMeasures mapFromJson(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonNode.toString(), new TypeReference<SonarMeasures>() {
        });
    }

}
