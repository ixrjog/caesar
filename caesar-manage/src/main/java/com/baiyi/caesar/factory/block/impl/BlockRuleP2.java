package com.baiyi.caesar.factory.block.impl;

import com.baiyi.caesar.common.base.BlockRuleLevel;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.factory.block.IBlockRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/1/26 4:30 下午
 * @Version 1.0
 *
 * 保留
 *
 */
@Slf4j
@Component("BlockRule-P2")
public class BlockRuleP2 extends BaseBlockRule implements IBlockRule {

    @Override
    public String getLevel() {
        return BlockRuleLevel.P2.getLevel();
    }

    @Override
    public BusinessWrapper<Boolean> verify(CsCiJob csCiJob) {
        return BusinessWrapper.SUCCESS;
    }
}