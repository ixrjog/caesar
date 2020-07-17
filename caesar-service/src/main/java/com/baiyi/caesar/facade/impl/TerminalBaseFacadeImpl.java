package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSessionInstance;
import com.baiyi.caesar.facade.TerminalBaseFacade;
import com.baiyi.caesar.service.terminal.OcTerminalSessionInstanceService;
import com.baiyi.caesar.service.terminal.OcTerminalSessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:26 下午
 * @Version 1.0
 */
@Service
public class TerminalBaseFacadeImpl implements TerminalBaseFacade {

    @Resource
    private OcTerminalSessionService ocTerminalSessionService;

    @Resource
    private OcTerminalSessionInstanceService ocTerminalSessionInstanceService;


    @Override
    public void addOcTerminalSession(OcTerminalSession ocTerminalSession) {
        ocTerminalSessionService.addOcTerminalSession(ocTerminalSession);
    }

    @Override
    public void updateOcTerminalSession(OcTerminalSession ocTerminalSession) {
        ocTerminalSessionService.updateOcTerminalSession(ocTerminalSession);
    }

    @Override
    public void addOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance) {
        ocTerminalSessionInstanceService.addOcTerminalSessionInstance(ocTerminalSessionInstance);
    }

    @Override
    public void updateOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance) {
        ocTerminalSessionInstanceService.updateOcTerminalSessionInstance(ocTerminalSessionInstance);
    }

    @Override
    public OcTerminalSessionInstance queryOcTerminalSessionInstanceByUniqueKey(String sessionId, String instanceId) {
        return ocTerminalSessionInstanceService.queryOcTerminalSessionInstanceByUniqueKey(sessionId, instanceId);
    }

}
