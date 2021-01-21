package com.baiyi.caesar.service.announcement.impl;

import com.baiyi.caesar.domain.generator.caesar.CsAnnouncement;
import com.baiyi.caesar.mapper.caesar.CsAnnouncementMapper;
import com.baiyi.caesar.service.announcement.CsAnnouncementService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/1/21 11:35 上午
 * @Version 1.0
 */
@Service
public class CsAnnouncementServiceImpl implements CsAnnouncementService {

    @Resource
    private CsAnnouncementMapper csAnnouncementMapper;

    @Override
    public List<CsAnnouncement> queryCsAnnouncementByValid() {
        Example example = new Example(CsAnnouncement.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", 1);
        return csAnnouncementMapper.selectByExample(example);
    }


}
