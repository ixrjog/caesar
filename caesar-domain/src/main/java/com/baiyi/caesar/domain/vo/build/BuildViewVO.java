package com.baiyi.caesar.domain.vo.build;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/3/17 6:02 下午
 * @Version 1.0
 */
public class BuildViewVO {


    @Data
    @Builder
    @ApiModel
    public static class JobBuildView implements BuildExecutorVO.IBuildExecutors {

        private Integer buildId;
        private int buildType;

        private List<BuildExecutorVO.BuildExecutor> executors;

        private String color;
        private Integer buildNumber;
        private Boolean building;
    }
}
