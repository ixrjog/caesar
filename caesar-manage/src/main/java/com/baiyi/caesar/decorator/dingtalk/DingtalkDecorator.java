package com.baiyi.caesar.decorator.dingtalk;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IDUtil;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.generator.caesar.CsDingtalk;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
import com.baiyi.caesar.service.dingtalk.CsDingtalkService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/7/27 3:42 下午
 * @Version 1.0
 */
@Component
public class DingtalkDecorator {

    @Resource
    private TagDecorator tagDecorator;

    @Resource
    private CsDingtalkService csDingtalkService;

    public void decorator(DingtalkVO.IDingtalk iDingtalk) {
        if (IDUtil.isEmpty(iDingtalk.getDingtalkId())) return;
        CsDingtalk csDingtalk = csDingtalkService.queryCsDingtalkById(iDingtalk.getDingtalkId());
        if (csDingtalk != null)
            iDingtalk.setDingtalk(BeanCopierUtil.copyProperties(csDingtalk, DingtalkVO.Dingtalk.class));
    }

    public DingtalkVO.Dingtalk decorator(DingtalkVO.Dingtalk dingtalk, Integer extend) {
        dingtalk.setDingtalkToken("");
        if (extend == 0) return dingtalk;
        // 装饰 标签
        tagDecorator.decorator(dingtalk);

        return dingtalk;
    }
}
