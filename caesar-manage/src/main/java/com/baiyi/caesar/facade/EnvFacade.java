package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.env.EnvParam;
import com.baiyi.caesar.domain.vo.env.EnvVO;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:28 下午
 * @Version 1.0
 */
public interface EnvFacade {

    DataTable<EnvVO.Env> queryEnvPage(EnvParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addEnv(EnvVO.Env env);

    BusinessWrapper<Boolean> updateEnv(EnvVO.Env env);

    BusinessWrapper<Boolean> deleteEnvById(int id);

    String queryEnvNameByType(Integer envType);
}
