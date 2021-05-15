package com.baiyi.caesar.packer.gitlab;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.packer.base.BaseDecorator;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabWebhook;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.vo.gitlab.GitlabEventVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/10 1:59 下午
 * @Version 1.0
 */
@Component
public class GitalbEventWarp extends BaseDecorator {

    public DataTable<GitlabEventVO.Event> wrap(DataTable<CsGitlabWebhook> table, IExtend iExtend) {
        List<GitlabEventVO.Event> page = table.getData().stream().map(e -> wrap(e, iExtend)).collect(Collectors.toList());
        return new DataTable<>(page, table.getTotalNum());
    }

    public GitlabEventVO.Event wrap(CsGitlabWebhook hook, IExtend iExtend) {
        GitlabEventVO.Event event = BeanCopierUtil.copyProperties(hook, GitlabEventVO.Event.class);
        if (iExtend.getExtend() == 1)
            decoratorBaseGitlab(event);
        return event;
    }
}
