package com.baiyi.caesar.domain.vo.dashboard;

import com.baiyi.caesar.domain.vo.build.CdJobBuildVO;
import com.baiyi.caesar.domain.vo.build.CiJobBuildVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/5 2:53 下午
 * @Version 1.0
 */
public class DashboardVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopCard implements Serializable {
        private static final long serialVersionUID = 4803038968871619073L;
        @ApiModelProperty(value = "应用总数")
        private int applicationTotal;
        @ApiModelProperty(value = "构建任务总数")
        private int buildJobTotal;
        @ApiModelProperty(value = "部署任务总数")
        private int deploymentJobTotal;
        @ApiModelProperty(value = "SCM项目总数")
        private int scmProjectTotal;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LatestTasks implements Serializable {
        private static final long serialVersionUID = 8963769393767259751L;
        @ApiModelProperty(value = "构建任务")
        private List<CiJobBuildVO.JobBuild> latestBuildTasks;

        @ApiModelProperty(value = "构建任务总执行数")
        private int buildTaskTotal;

        @ApiModelProperty(value = "部署任务")
        private List<CdJobBuildVO.JobBuild> latestDeploymentTasks;

        @ApiModelProperty(value = "部署任务总执行数")
        private int deploymentTaskTotal;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TaskExecutionGroupByHour implements Serializable {

        private static final long serialVersionUID = 8995436097311659778L;

        @ApiModelProperty(value = "构建任务按小时分布")
        private List<BuildTaskGroupByHour> buildTaskGroupByHours;
        @ApiModelProperty(value = "部署任务按小时分布")
        private List<BuildTaskGroupByHour> deploymentTaskGroupByHours;
    }

}
