package com.baiyi.caesar.factory.block.impl;

import com.baiyi.caesar.common.base.BlockRuleLevel;
import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.common.base.JobType;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.factory.block.IBlockRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/1/26 4:19 下午
 * @Version 1.0
 * <p>
 * P3规则:
 * 任务范围: Java类型发布（gray,prod）
 * 用户限制: 限制普通用户发布，管理员可发布
 */
@Slf4j
@Component("BlockRule-P3")
public class BlockRuleP3 extends BaseBlockRule implements IBlockRule {

    @Override
    public String getLevel() {
        return BlockRuleLevel.P3.getLevel();
    }

    @Override
    public BusinessWrapper<Boolean> verify(CsCiJob csCiJob) {
        // 判断任务类型
        if (!JobType.JAVA.getType().equalsIgnoreCase(csCiJob.getJobType()))
            return BusinessWrapper.SUCCESS;
        // 判断任务环境
        String envName = getEnvNameByType(csCiJob.getEnvType());
        if (Global.ENV_PROD.equals(envName))
            return new BusinessWrapper<>(ErrorEnum.BLOCK_RULE_PROHIBIT_PROD_BUILD);
        //  if (Global.ENV_GRAY.equals(envName))
        //      return new BusinessWrapper<>(ErrorEnum.BLOCK_RULE_PROHIBIT_GRAY_BUILD);

        if (isPlatformAdmin()) // 允许平台管理员操作
            return BusinessWrapper.SUCCESS;

        if (isApplicationAdmin(csCiJob.getApplicationId())) // 允许应用管理员操作
            return BusinessWrapper.SUCCESS;

        return new BusinessWrapper(ErrorEnum.BLOCK_RULE_P3);
    }

}