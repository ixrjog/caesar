package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSessionInstance;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:26 下午
 * @Version 1.0
 */
public interface TerminalBaseFacade {

    void addOcTerminalSession(OcTerminalSession ocTerminalSession);

    void updateOcTerminalSession(OcTerminalSession ocTerminalSession);

    void addOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance);

    void updateOcTerminalSessionInstance(OcTerminalSessionInstance ocTerminalSessionInstance);

    OcTerminalSessionInstance queryOcTerminalSessionInstanceByUniqueKey(String sessionId, String instanceId);
}
