package com.baiyi.caesar.decorator.dingtalk;

import com.baiyi.caesar.common.base.BusinessType;
import com.baiyi.caesar.decorator.tag.TagDecorator;
import com.baiyi.caesar.domain.vo.dingtalk.DingtalkVO;
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

    public DingtalkVO.Dingtalk decorator(DingtalkVO.Dingtalk dingtalk, Integer extend) {
        dingtalk.setDingtalkToken("");
        if (extend == 0) return dingtalk;
        // 装饰 标签

        dingtalk.setBusinessType(BusinessType.DINGTALK.getType());
        tagDecorator.decorator(dingtalk);

        return dingtalk;
    }
}
