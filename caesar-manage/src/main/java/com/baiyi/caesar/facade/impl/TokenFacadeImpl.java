package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.facade.TokenFacade;
import com.baiyi.caesar.service.user.OcUserTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/4/19 11:42 上午
 * @Version 1.0
 */
@Service
public class TokenFacadeImpl implements TokenFacade {

    @Resource
    private OcUserTokenService ocUserTokenService;

    @Override
    public void revokeAllUserToken() {
        ocUserTokenService.revokeAllUserToken();

    }


}
