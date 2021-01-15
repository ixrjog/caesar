package com.baiyi.caesar.sonar;

import com.baiyi.caesar.domain.vo.sonar.SonarMeasures;

/**
 * @Author baiyi
 * @Date 2020/12/24 3:17 下午
 * @Version 1.0
 */
public interface SonarQubeServer {

   SonarMeasures queryMeasuresComponent(String projectKey);
}
