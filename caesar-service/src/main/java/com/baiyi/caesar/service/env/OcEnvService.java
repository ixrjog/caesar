package com.baiyi.caesar.service.env;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.param.env.EnvParam;

/**
 * @Author baiyi
 * @Date 2020/1/10 2:16 下午
 * @Version 1.0
 */
public interface OcEnvService {

    OcEnv queryOcEnvById(Integer id);

    OcEnv queryOcEnvByType(Integer type);

    OcEnv queryOcEnvByName(String name);

    DataTable<OcEnv> queryOcEnvByParam(EnvParam.PageQuery pageQuery);

    void addOcEnv(OcEnv ocEnv);

    void updateOcEnv(OcEnv ocEnv);

    void deleteOcEnvById(int id);
}
