package com.baiyi.caesar.factory.block;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;

/**
 * @Author baiyi
 * @Date 2021/1/26 3:51 下午
 * @Version 1.0
 */
public interface IBlockRule {

    String getLevel();

    BusinessWrapper<Boolean> verify(CsCiJob csCiJob);

    BusinessWrapper<Boolean> verify(CsCdJob csCdJob);
}
