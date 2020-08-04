package com.baiyi.caesar.domain.vo.aliyun;

import com.baiyi.caesar.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/31 4:23 下午
 * @Version 1.0
 */
public class OssBucketVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Bucket {

        private List<TagVO.Tag> tags;

        private Integer id;

        private String name;

        private String bucketLocation;

        private String extranetEndpoint;

        private String intranetEndpoint;

        private String bucketRegion;

        private Boolean isActive;

        private Date createTime;

        private Date updateTime;

        private String comment;
    }
}
