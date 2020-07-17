package com.baiyi.caesar.service.kubernetes;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesServicePort;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 9:50 上午
 * @Version 1.0
 */
public interface OcKubernetesServicePortService {

    void addOcKubernetesServicePort(OcKubernetesServicePort ocKubernetesServicePort);

    void updateOcKubernetesServicePort(OcKubernetesServicePort ocKubernetesServicePort);

    void deleteOcKubernetesServicePortById(int id);

    List<OcKubernetesServicePort> queryOcKubernetesServicePortByServiceId(int serviceId);

}
