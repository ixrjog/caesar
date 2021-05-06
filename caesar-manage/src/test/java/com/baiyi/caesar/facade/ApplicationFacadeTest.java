package com.baiyi.caesar.facade;

import com.baiyi.caesar.BaseUnit;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author baiyi
 * @Date 2021/5/6 4:12 下午
 * @Version 1.0
 */
class ApplicationFacadeTest extends BaseUnit {

    @Resource
    private ApplicationFacade applicationFacade;

    @Test
    void syncApplicationScmMemberTest(){
        applicationFacade.syncApplicationScmMember(91);
    }

}