package com.baiyi.caesar.service.announcement;

import com.baiyi.caesar.domain.generator.caesar.CsAnnouncement;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/1/21 11:33 上午
 * @Version 1.0
 */
public interface CsAnnouncementService {

    /**
     * 查询有效的公告
     * @return
     */
    List<CsAnnouncement> queryCsAnnouncementByValid();
}
