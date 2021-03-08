package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.param.env.EnvParam;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.facade.EnvFacade;
import com.baiyi.caesar.service.env.OcEnvService;
import com.baiyi.caesar.service.server.OcServerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 5:29 下午
 * @Version 1.0
 */
@Service
public class EnvFacadeImpl implements EnvFacade {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcServerService ocServerService;

    public static final boolean ACTION_ADD = true;
    public static final boolean ACTION_UPDATE = false;

    @Override
    public DataTable<EnvVO.Env> queryEnvPage(EnvParam.PageQuery pageQuery) {
        DataTable<OcEnv> table = ocEnvService.queryOcEnvByParam(pageQuery);
        List<EnvVO.Env> page = BeanCopierUtil.copyListProperties(table.getData(), EnvVO.Env.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> addEnv(EnvVO.Env env) {
        return saveEnv(env, ACTION_ADD);
    }

    @Override
    public BusinessWrapper<Boolean> updateEnv(EnvVO.Env env) {
        return saveEnv(env, ACTION_UPDATE);
    }

    private BusinessWrapper<Boolean> saveEnv(EnvVO.Env env, boolean action) {
        OcEnv checkOcEnvName = ocEnvService.queryOcEnvByName(env.getEnvName());
        OcEnv ocEnv = BeanCopierUtil.copyProperties(env, OcEnv.class);
        // 对象存在 && 新增
        if (checkOcEnvName != null && action)
            return new BusinessWrapper<>(ErrorEnum.ENV_NAME_ALREADY_EXIST);
        if (action) {
            ocEnvService.addOcEnv(ocEnv);
        } else {
            ocEnvService.updateOcEnv(ocEnv);
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteEnvById(int id) {
        OcEnv ocEnv = ocEnvService.queryOcEnvById(id);
        if (ocEnv == null)
            return new BusinessWrapper<>(ErrorEnum.ENV_NOT_EXIST);
        // 判断是否位基本数据
        if (ocEnv.getEnvType() == 0)
            new BusinessWrapper<>(ErrorEnum.ENV_IS_DEFAULT);
        // 判断server绑定的资源
        if (ocServerService.countByEnvType(ocEnv.getEnvType()) == 0) {
            ocEnvService.deleteOcEnvById(id);
            return BusinessWrapper.SUCCESS;
        } else {
            return new BusinessWrapper<>(ErrorEnum.ENV_HAS_USED);
        }
    }

    @Override
    public String queryEnvNameByType(Integer envType) {
        if (envType == null)
            envType = 0;
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(envType);
        if (ocEnv != null) return ocEnv.getEnvName();
        return ocEnvService.queryOcEnvByType(0).getEnvName();
    }
}
