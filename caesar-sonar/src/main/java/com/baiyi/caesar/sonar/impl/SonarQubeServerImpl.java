package com.baiyi.caesar.sonar.impl;

import com.baiyi.caesar.domain.vo.sonar.SonarMeasures;
import com.baiyi.caesar.sonar.SonarQubeServer;
import com.baiyi.caesar.sonar.handler.SonarQubeHandler;
import com.baiyi.caesar.sonar.mapper.SonarMeasuresMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/12/24 3:17 下午
 * @Version 1.0
 */
@Component
public class SonarQubeServerImpl implements SonarQubeServer {

    public static final String API = "api/measures/component";

    public static final String[] METRIC_KEYS = { "alert_status",
            "quality_gate_details", "bugs","new_bugs","reliability_rating",
            "new_reliability_rating","vulnerabilities", "new_vulnerabilities","security_rating",
            "new_security_rating","security_hotspots", "new_security_hotspots","code_smells",
            "new_code_smells","sqale_rating", "new_maintainability_rating",
            "sqale_index","new_technical_debt", "coverage","new_coverage","new_lines_to_cover",
            "tests","duplicated_lines_density","new_duplicated_lines_density",
            "duplicated_blocks","ncloc","ncloc_language_distribution","projects","new_lines"};

    @Override
    public SonarMeasures queryMeasuresComponent(String projectKey) {
        try {
            JsonNode jsonNode = SonarQubeHandler.httpGetExecutor(API, buildMeasuresComponentParam(projectKey));
            return new SonarMeasuresMapper().mapFromJson(jsonNode);
        } catch (IOException e) {
            return new SonarMeasures();
        }
    }

    private Map<String, String> buildMeasuresComponentParam(String projectKey) {
        Map<String, String> paramMap = Maps.newHashMap();
        paramMap.put("additionalFields", "metrics,periods");
        paramMap.put("component", projectKey);
        paramMap.put("metricKeys", Joiner.on(",").join(METRIC_KEYS));
        return paramMap;
    }
}
