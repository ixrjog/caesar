package com.baiyi.caesar.application;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.type.ApplicationResEnum;
import com.baiyi.caesar.domain.generator.caesar.CsApplicationResource;
import com.baiyi.caesar.service.application.CsApplicationResourceService;
import com.baiyi.caesar.service.application.CsApplicationServerGroupService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/6 1:30 下午
 * @Version 1.0
 */
public class AppliationTest extends BaseUnit {

    @Resource
    private CsApplicationResourceService csApplicationResourceService;

    @Resource
    private CsApplicationServerGroupService csApplicationServerGroupService;

    @Test
    void initApplicationServerGroupRes() {
        csApplicationServerGroupService.selectAll().forEach(e -> {
            if (csApplicationResourceService.queryApplicationResourceByUniqueKey(e.getApplicationId()
                    , ApplicationResEnum.SERVER_GROUP.getType(), e.getServerGroupName()) != null)
                return;

            CsApplicationResource r = new CsApplicationResource();
            r.setApplicationId(e.getApplicationId());
            r.setSource(e.getSource());
            r.setResId(e.getServerGroupId());
            r.setResName(e.getServerGroupName());
            r.setResKey(e.getServerGroupName());
            r.setResComment(e.getComment());
            r.setResType(ApplicationResEnum.SERVER_GROUP.getType());
            csApplicationResourceService.addApplicationResource(r);
        });
    }


}
