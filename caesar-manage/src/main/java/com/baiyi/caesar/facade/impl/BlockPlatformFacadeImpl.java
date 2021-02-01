package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.CsBlockPlatform;
import com.baiyi.caesar.domain.vo.platform.BlockPlatformVO;
import com.baiyi.caesar.facade.BlockPlatformFacade;
import com.baiyi.caesar.factory.block.BlockRuleHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/1/28 10:15 上午
 * @Version 1.0
 */
@Service
public class BlockPlatformFacadeImpl implements BlockPlatformFacade {

    @Resource
    private BlockRuleHandler blockRuleHandler;

    @Override
    public BlockPlatformVO.BlockPlatformStatus queryBlockPlatformStatus() {
        List<CsBlockPlatform> csBlockPlatforms = blockRuleHandler.queryBlockPlatformByValid();
        if (!CollectionUtils.isEmpty(csBlockPlatforms)) {
            return BlockPlatformVO.BlockPlatformStatus.builder()
                    .blockPlatforms(BeanCopierUtils.copyListProperties(csBlockPlatforms, BlockPlatformVO.BlockPlatform.class))
                    .isShow(true)
                    .build();
        } else {
            return BlockPlatformVO.BlockPlatformStatus.builder()
                    .build();
        }
    }


}
