package com.baiyi.caesar.domain.vo.announcement;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/1/21 1:53 下午
 * @Version 1.0
 */
public class AnnouncementVO {

    @Data
    @Builder
    @ApiModel
    public static class AnnouncementCarousel {

        private Boolean isShow;
        private List<Announcement> announcements;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Announcement {

        private Integer id;
        /**
         * 公告标题
         */
        private String title;

        private Date startTime;

        private Date endTime;

        /**
         * 公告内容
         */
        private String content;

    }

}
