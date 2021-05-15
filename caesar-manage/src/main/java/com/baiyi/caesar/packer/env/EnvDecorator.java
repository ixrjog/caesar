package com.baiyi.caesar.packer.env;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.service.env.OcEnvService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/3/17 10:29 上午
 * @Version 1.0
 */
@Component
public class EnvDecorator {

    @Resource
    private OcEnvService ocEnvService;

    public void decorator(EnvVO.IEnv iEnv) {
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(iEnv.getEnvType());
        if (ocEnv == null) return;
        iEnv.setEnv(BeanCopierUtil.copyProperties(ocEnv, EnvVO.Env.class));
    }
}
