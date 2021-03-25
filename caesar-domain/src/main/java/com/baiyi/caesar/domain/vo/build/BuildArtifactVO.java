package com.baiyi.caesar.domain.vo.build;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/8/31 2:57 下午
 * @Version 1.0
 */
public class BuildArtifactVO {

    public interface IBuildArtifacts {

        Integer getBuildId();

        int getBuildType();

        void setArtifacts(List<BuildArtifact> artifacts);

        void setNoArtifact(Boolean noArtifact); // 没有构件（前端下拉列表禁止选中）

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BuildArtifact {

        private String artifactFileSize; // 产出物文件容量
        private String ossUrl;

        private Integer id;
        private Integer buildType;
        private Integer buildId;
        private Integer jobId;
        private String jobName;
        private String artifactDisplayPath;
        private String artifactFileName;
        private String artifactRelativePath;
        private Long artifactSize;
        private String storagePath;
        private Date updateTime;
        private Date createTime;
    }
}
