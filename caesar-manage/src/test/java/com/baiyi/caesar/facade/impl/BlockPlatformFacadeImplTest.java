package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.vo.platform.BlockPlatformVO;
import com.baiyi.caesar.facade.BlockPlatformFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/1/28 2:53 下午
 * @Version 1.0
 */
class BlockPlatformFacadeImplTest extends BaseUnit {

    @Resource
    private BlockPlatformFacade blockPlatformFacade;

    @Test
    void queryBlockPlatformStatusTest() {
        BlockPlatformVO.BlockPlatformStatus status = blockPlatformFacade.queryBlockPlatformStatus();
        System.err.println(status);
    }


}