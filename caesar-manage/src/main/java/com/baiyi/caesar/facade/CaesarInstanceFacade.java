package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.vo.caesar.HealthVO;

/**
 * @Author baiyi
 * @Date 2020/9/8 10:39 上午
 * @Version 1.0
 */
public interface CaesarInstanceFacade {

   HealthVO.Health checkHealth();

   boolean isHealth();
}
