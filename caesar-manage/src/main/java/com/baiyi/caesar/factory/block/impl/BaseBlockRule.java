package com.baiyi.caesar.factory.block.impl;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.generator.caesar.CsCdJob;
import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.facade.ApplicationFacade;
import com.baiyi.caesar.facade.EnvFacade;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.UserPermissionFacade;
import com.baiyi.caesar.factory.block.BlockRuleFactory;
import com.baiyi.caesar.factory.block.IBlockRule;
import com.baiyi.caesar.service.jenkins.CsCiJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/1/26 3:54 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseBlockRule implements IBlockRule, InitializingBean {

    @Resource
    private EnvFacade envFacade;

    @Resource
    private UserFacade userFacade;

    @Resource
    private UserPermissionFacade userPermissionFacade;

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private CsCiJobService csCiJobService;

    @Override
    public BusinessWrapper<Boolean> verify(CsCdJob csCdJob) {
        CsCiJob csCiJob = csCiJobService.queryCsCiJobById(csCdJob.getCiJobId());
        return verify(csCiJob);
    }

    protected boolean isPlatformAdmin() {
        OcUser ocUser = userFacade.getOcUserBySession();
        return userPermissionFacade.checkAccessLevel(ocUser, AccessLevel.OPS.getLevel()).isSuccess();
    }

    protected boolean isApplicationAdmin(int applicationId) {
        OcUser ocUser = userFacade.getOcUserBySession();
        return ocUser != null && applicationFacade.isApplicationAdmin(applicationId, ocUser.getId());
    }

    protected String getEnvNameByType(int envType) {
        return envFacade.queryEnvNameByType(envType);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        BlockRuleFactory.register(this);
    }

}
