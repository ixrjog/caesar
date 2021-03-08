package com.baiyi.caesar.decorator.terminal;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSession;
import com.baiyi.caesar.domain.generator.caesar.OcTerminalSessionInstance;
import com.baiyi.caesar.domain.vo.term.TerminalSessionVO;
import com.baiyi.caesar.service.terminal.OcTerminalSessionInstanceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:33 下午
 * @Version 1.0
 */
@Component
public class TerminalSessionDecorator {

    @Resource
    private OcTerminalSessionInstanceService ocTerminalSessionInstanceService;

    @Resource
    private TerminalSessionInstanceDecorator terminalSessionInstanceDecorator;

    public TerminalSessionVO.TerminalSession decorator(OcTerminalSession ocTerminalSession, Integer extend) {
        TerminalSessionVO.TerminalSession terminalSession = BeanCopierUtil.copyProperties(ocTerminalSession, TerminalSessionVO.TerminalSession.class);
        if (extend == 1) {
            List<OcTerminalSessionInstance> instanceList = ocTerminalSessionInstanceService.queryOcTerminalSessionInstanceBySessionId(ocTerminalSession.getSessionId());
            terminalSession.setSessionInstances(
                    instanceList.stream().map(i ->
                            terminalSessionInstanceDecorator.decorator(i, 0)
                    ).collect(Collectors.toList()));
        }
        return terminalSession;
    }


}
