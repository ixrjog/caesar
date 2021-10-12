package com.baiyi.caesar.domain.vo.aliyun;

import com.baiyi.caesar.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 8:03 下午
 * @Since 1.0
 */
public class AliyunCRVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance implements TagVO.ITags {

        private int businessType;

        @Override
        public int getBusinessId() {
            return id;
        }

        private List<TagVO.Tag> tags;

        private Integer id;

        private String regionId;

        private String instanceName;

        private String instanceId;

        private String internetEndpoint;

        private String intranetEndpoint;

        private String vpcEndpoint;

        private Boolean isActive;

        private Date createTime;

        private Date updateTime;
    }
}
