package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.builder.CaesarInstanceBuilder;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.HostUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsInstance;
import com.baiyi.caesar.domain.param.caesar.CaesarInstanceParam;
import com.baiyi.caesar.domain.vo.caesar.CaesarVO;
import com.baiyi.caesar.domain.vo.caesar.HealthVO;
import com.baiyi.caesar.facade.CaesarInstanceFacade;
import com.baiyi.caesar.service.instance.CsInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/7 11:06 上午
 * @Version 1.0
 */
@Slf4j
@Service
public class CaesarInstanceFacadeImpl implements CaesarInstanceFacade, InitializingBean {

    @Resource
    private CsInstanceService csInstanceService;

    private static final String HEALTH_OK = "OK";
    private static final String HEALTH_ERROR = "ERROR";
    private static final String HEALTH_INACTIVE = "INACTIVE";

    @Override
    public BusinessWrapper<Boolean> setCaesarInstanceActive(int id) {
        CsInstance csInstance = csInstanceService.queryCsInstanceById(id);
        csInstance.setIsActive(!csInstance.getIsActive());
        csInstanceService.updateCsInstance(csInstance);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public boolean isHealth() {
        HealthVO.Health health = checkHealth();
        return HEALTH_OK.equals(health.getStatus());
    }

    @Override
    public HealthVO.Health checkHealth() {
        try {
            InetAddress inetAddress = HostUtil.getInetAddress();
            CsInstance csInstance = csInstanceService.queryCsInstanceByHostIp(inetAddress.getHostAddress());
            if (csInstance == null)
                return getHealth(HEALTH_ERROR, false);
            if (csInstance.getIsActive()) {
                return getHealth(HEALTH_OK, true);
            } else {
                return getHealth(HEALTH_INACTIVE, false);
            }
        } catch (UnknownHostException ignored) {
        }
        return getHealth(HEALTH_ERROR, false);
    }

    @Override
    public DataTable<CaesarVO.Instance> queryCaesarInstancePage(CaesarInstanceParam.CaesarInstancePageQuery pageQuery) {
        DataTable<CsInstance> table = csInstanceService.queryCsInstanceByParam(pageQuery);
        List<CaesarVO.Instance> page = BeanCopierUtil.copyListProperties(table.getData(), CaesarVO.Instance.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    private HealthVO.Health getHealth(String status, boolean isHealth) {
        HealthVO.Health health = new HealthVO.Health();
        health.setStatus(status);
        health.setHealth(isHealth);
        return health;
    }

    private void register() {
        try {
            InetAddress inetAddress = HostUtil.getInetAddress();
            if (csInstanceService.queryCsInstanceByHostIp(inetAddress.getHostAddress()) != null) return;
            CsInstance csInstance = CaesarInstanceBuilder.build(inetAddress);
            csInstanceService.addCsInstance(csInstance);
        } catch (UnknownHostException ignored) {
        }
    }

    @Override
    public void afterPropertiesSet() {
        this.register();
    }

}
