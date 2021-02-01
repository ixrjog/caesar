package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.vo.platform.BlockPlatformVO;

/**
 * @Author baiyi
 * @Date 2021/1/28 10:14 上午
 * @Version 1.0
 */
public interface BlockPlatformFacade {

    BlockPlatformVO.BlockPlatformStatus queryBlockPlatformStatus();
}
