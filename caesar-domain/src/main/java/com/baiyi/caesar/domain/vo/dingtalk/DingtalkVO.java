package com.baiyi.caesar.domain.vo.dingtalk;

import com.baiyi.caesar.domain.vo.tag.TagVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:39 下午
 * @Version 1.0
 */
public class DingtalkVO {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Dingtalk {

        private List<TagVO.Tag> tags;

        private Integer id;

        private String name;

        private String dingtalkToken;

        private Date createTime;

        private Date updateTime;

        private String comment;

    }
}
