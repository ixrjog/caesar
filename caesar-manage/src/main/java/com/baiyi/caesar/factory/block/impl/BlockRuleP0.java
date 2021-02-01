package com.baiyi.caesar.factory.block.impl;

import com.baiyi.caesar.common.base.BlockRuleLevel;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.factory.block.IBlockRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/1/26 4:32 下午
 * @Version 1.0
 * <p>
 * P0规则:
 * 任务范围: All
 * 用户限制: All
 */
@Slf4j
@Component("BlockRule-P0")
public class BlockRuleP0 extends BaseBlockRule implements IBlockRule {

    @Override
    public String getLevel() {
        return BlockRuleLevel.P0.getLevel();
    }

    @Override
    public BusinessWrapper<Boolean> verify(CsCiJob csCiJob) {
        return new BusinessWrapper(ErrorEnum.BLOCK_RULE_P0);
    }

}