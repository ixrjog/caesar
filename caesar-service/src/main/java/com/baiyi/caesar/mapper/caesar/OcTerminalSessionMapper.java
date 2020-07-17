package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.domain.param.term.TermSessionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcTerminalSessionMapper extends Mapper<OcTerminalSession> {

    List<OcTerminalSession> queryTerminalSessionByParam(TermSessionParam.PageQuery pageQuery);
}