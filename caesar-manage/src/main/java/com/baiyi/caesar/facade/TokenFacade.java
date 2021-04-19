package com.baiyi.caesar.facade;

/**
 * @Author baiyi
 * @Date 2021/4/19 11:42 上午
 * @Version 1.0
 */
public interface TokenFacade {

    /**
     * 吊销所有用户Token
     */
    void revokeAllUserToken();
}
