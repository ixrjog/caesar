package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.caesar.CaesarInstanceParam;
import com.baiyi.caesar.domain.vo.caesar.CaesarVO;
import com.baiyi.caesar.domain.vo.caesar.HealthVO;

/**
 * @Author baiyi
 * @Date 2020/9/8 10:39 上午
 * @Version 1.0
 */
public interface CaesarInstanceFacade {

   HealthVO.Health checkHealth();

   DataTable<CaesarVO.Instance> queryCaesarInstancePage(CaesarInstanceParam.CaesarInstancePageQuery pageQuery);

   boolean isHealth();

   BusinessWrapper<Boolean> setCaesarInstanceActive(int id);
}
