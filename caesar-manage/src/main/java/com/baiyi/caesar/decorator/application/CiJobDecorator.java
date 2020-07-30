package com.baiyi.caesar.decorator.application;

import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.common.util.IDUtils;
import com.baiyi.caesar.domain.generator.caesar.CsDingtalk;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.vo.application.CiJobVO;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import com.baiyi.caesar.service.env.OcEnvService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/29 2:08 下午
 * @Version 1.0
 */
@Component
public class CiJobDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private CsDingtalkService csDingtalkService;

    public CiJobVO.CiJob decorator(CiJobVO.CiJob ciJob, Integer extend) {

        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ciJob.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            ciJob.setEnv(env);
        }

        if (!IDUtils.isEmpty(ciJob.getDingtalkId())) {
            CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(ciJob.getDingtalkId());
            if (csDingtalk != null)
                ciJob.setDingtalk(BeanCopierUtils.copyProperties(csDingtalk, DingtalkVO.Dingtalk.class));
        }
        return ciJob;
    }
}
