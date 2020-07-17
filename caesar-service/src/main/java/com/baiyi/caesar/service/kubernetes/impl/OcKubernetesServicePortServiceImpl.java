package com.baiyi.caesar.service.kubernetes.impl;

import com.baiyi.caesar.domain.generator.caesar.OcKubernetesServicePort;
import com.baiyi.caesar.mapper.caesar.OcKubernetesServicePortMapper;
import com.baiyi.caesar.service.kubernetes.OcKubernetesServicePortService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 9:50 上午
 * @Version 1.0
 */
@Service
public class OcKubernetesServicePortServiceImpl implements OcKubernetesServicePortService {

    @Resource
    private OcKubernetesServicePortMapper ocKubernetesServicePortMapper;

    @Override
    public void addOcKubernetesServicePort(OcKubernetesServicePort ocKubernetesServicePort) {
        ocKubernetesServicePortMapper.insert(ocKubernetesServicePort);
    }

    @Override
    public void updateOcKubernetesServicePort(OcKubernetesServicePort ocKubernetesServicePort) {
        ocKubernetesServicePortMapper.updateByPrimaryKey(ocKubernetesServicePort);
    }

    @Override
    public void deleteOcKubernetesServicePortById(int id) {
        ocKubernetesServicePortMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<OcKubernetesServicePort> queryOcKubernetesServicePortByServiceId(int serviceId) {
        Example example = new Example(OcKubernetesServicePort.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serviceId", serviceId);
        return ocKubernetesServicePortMapper.selectByExample(example);
    }
}
