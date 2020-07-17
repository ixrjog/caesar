package com.baiyi.caesar.terminal;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.facade.TerminalFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/8 12:50 下午
 * @Version 1.0
 */
public class TerminalTest extends BaseUnit {

    @Resource
    private TerminalFacade terminalFacade;

    @Test
    void taskTest(){
        terminalFacade.closeInvalidSessionTask();
    }
}
