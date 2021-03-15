package com.baiyi.caesar.task;

import com.baiyi.caesar.facade.CaesarInstanceFacade;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/6/8 1:37 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseTask {

    @Resource
    private CaesarInstanceFacade caesarInstanceFacade;

    protected boolean isHealth() {
        return caesarInstanceFacade.isHealth();
    }

}
