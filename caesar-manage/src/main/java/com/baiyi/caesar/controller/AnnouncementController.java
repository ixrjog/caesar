package com.baiyi.caesar.controller;

import com.baiyi.caesar.domain.HttpResult;
import com.baiyi.caesar.domain.vo.announcement.AnnouncementVO;
import com.baiyi.caesar.facade.AnnouncementFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/1/21 2:51 下午
 * @Version 1.0
 */
@RestController
@RequestMapping("/announcement")
@Api(tags = "公告")
public class AnnouncementController {

    @Resource
    private AnnouncementFacade announcementFacade;

    @ApiOperation(value = "查询公告跑马灯")
    @GetMapping(value = "/carousel/query", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AnnouncementVO.AnnouncementCarousel> queryAnnouncementCarousel() {
        return new HttpResult<>(announcementFacade.queryAnnouncementCarousel());
    }
}
