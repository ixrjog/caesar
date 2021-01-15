package com.baiyi.caesar.domain.vo.sonar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/12/24 5:03 下午
 * @Version 1.0
 */
public class SonarEntry {


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Component {

        private String id;
        private String key;
        private String name;
        private String qualifier;
        private String description;
        private List<Measure> measures;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Measure extends BasePeriod{

        private String metric;
        private String value;
        private List<Period> periods;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Period extends BasePeriod {

        private Integer index;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BasePeriod {

        private String value;
        private Boolean bestValue;
    }

}
