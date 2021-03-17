package com.baiyi.caesar.decorator.sonar;

import com.baiyi.caesar.bo.SonarParamsBO;
import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.vo.sonar.SonarEntry;
import com.baiyi.caesar.domain.vo.sonar.SonarMeasures;
import com.baiyi.caesar.domain.vo.sonar.SonarQubeVO;
import com.baiyi.caesar.service.application.CsApplicationService;
import com.baiyi.caesar.sonar.SonarQubeServer;
import com.baiyi.caesar.sonar.config.SonarConfig;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/17 3:15 下午
 * @Version 1.0
 */
@Component
public class SonarQubeDecorator {

    @Resource
    private SonarQubeServer sonarQubeServer;

    @Resource
    private CsApplicationService csApplicationService;

    @Resource
    private SonarConfig sonarConfig;

    public void decorator(SonarQubeVO.ISonarQube iSonarQube){
        if(!iSonarQube.enableSonar()) return;
        CsApplication csApplication = csApplicationService.queryCsApplicationById(iSonarQube.getApplicationId());
        String projectKey = Joiner.on("_").join(csApplication.getApplicationKey(), iSonarQube.getJobKey());
        iSonarQube.setSonarQube(buildSonarQube(projectKey));
    }

    private SonarQubeVO.SonarQube buildSonarQube(String projectKey) {
        SonarMeasures sonarMeasures = sonarQubeServer.queryMeasuresComponent(projectKey);
        return SonarQubeVO.SonarQube.builder()
                .measures(convertMeasures(sonarMeasures))
                .alertStatus(new SonarParamsBO(projectKey, "alert_status").toString())
                // http://sonar.xinc818.com/dashboard?id=DATA-CENTER_data-center-server-dev
                .projectUrl(Joiner.on("/").join(sonarConfig.getUrl(), "dashboard?id=") + projectKey)
                .build();
    }

    private Map<String, SonarEntry.Measure> convertMeasures(SonarMeasures sonarMeasures) {
        if (sonarMeasures.getComponent() == null || CollectionUtils.isEmpty(sonarMeasures.getComponent().getMeasures()))
            return Maps.newHashMap();
        return sonarMeasures.getComponent().getMeasures().stream().collect(Collectors.toMap(SonarEntry.Measure::getMetric, a -> a, (k1, k2) -> k1));
    }
}
