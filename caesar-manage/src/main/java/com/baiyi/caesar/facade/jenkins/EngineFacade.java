package com.baiyi.caesar.facade.jenkins;

import com.baiyi.caesar.domain.vo.tree.EngineVO;

/**
 * @Author baiyi
 * @Date 2020/8/31 5:46 下午
 * @Version 1.0
 */

public interface EngineFacade {

    EngineVO.Children buildEngineChart();

}
