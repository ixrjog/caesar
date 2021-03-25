package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.CsAnnouncement;
import com.baiyi.caesar.domain.vo.announcement.AnnouncementVO;
import com.baiyi.caesar.facade.AnnouncementFacade;
import com.baiyi.caesar.service.announcement.CsAnnouncementService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/1/21 11:43 上午
 * @Version 1.0
 */
@Service
public class AnnouncementFacadeImpl implements AnnouncementFacade {

    @Resource
    private CsAnnouncementService csAnnouncementService;

    public AnnouncementVO.AnnouncementCarousel queryAnnouncementCarousel() {
        List<CsAnnouncement> csAnnouncements = filter(csAnnouncementService.queryCsAnnouncementByValid());
        return AnnouncementVO.AnnouncementCarousel.builder()
                .announcements(BeanCopierUtil.copyListProperties(csAnnouncements, AnnouncementVO.Announcement.class))
                .isShow(!CollectionUtils.isEmpty(csAnnouncements))
                .build();
    }

    private List<CsAnnouncement> filter(List<CsAnnouncement> csAnnouncements) {
        if (CollectionUtils.isEmpty(csAnnouncements))
            return csAnnouncements;
        return csAnnouncements.stream().filter(e ->
                e.getStartTime() == null || e.getEndTime() == null
        ).collect(Collectors.toList());
    }

}
