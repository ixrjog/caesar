package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.term.TermSessionParam;
import com.baiyi.caesar.domain.vo.term.TerminalSessionInstanceVO;
import com.baiyi.caesar.domain.vo.term.TerminalSessionVO;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:30 下午
 * @Version 1.0
 */
public interface TerminalFacade {

    DataTable<TerminalSessionVO.TerminalSession> queryTerminalSessionPage(TermSessionParam.PageQuery pageQuery);

    TerminalSessionInstanceVO.TerminalSessionInstance  querySessionInstanceById(int id);

    void closeInvalidSessionTask();
}
