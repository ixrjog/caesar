package com.baiyi.caesar.service.terminal;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.domain.param.term.TermSessionParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:18 下午
 * @Version 1.0
 */
public interface OcTerminalSessionService {

    DataTable<OcTerminalSession> queryTerminalSessionByParam(TermSessionParam.PageQuery pageQuery);

    OcTerminalSession queryOcTerminalSessionBySessionId(String sessionId);

    List<OcTerminalSession> queryOcTerminalSessionByActive();

    void addOcTerminalSession(OcTerminalSession ocTerminalSession);

    void updateOcTerminalSession(OcTerminalSession ocTerminalSession);
}
