package com.baiyi.caesar.service.kubernetes.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplicationInstance;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationInstanceParam;
import com.baiyi.caesar.mapper.caesar.OcKubernetesApplicationInstanceMapper;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:26 下午
 * @Version 1.0
 */
@Service
public class OcKubernetesApplicationInstanceServiceImpl implements OcKubernetesApplicationInstanceService {

    @Resource
    private OcKubernetesApplicationInstanceMapper ocKubernetesApplicationInstanceMapper;

    @Override
    public DataTable<OcKubernetesApplicationInstance> queryOcKubernetesApplicationInstanceByParam(KubernetesApplicationInstanceParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcKubernetesApplicationInstance> list = ocKubernetesApplicationInstanceMapper.queryOcKubernetesApplicationInstanceByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<OcKubernetesApplicationInstance> queryOcKubernetesApplicationInstanceByApplicationId(int applicationId) {
        Example example = new Example(OcKubernetesApplicationInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationId", applicationId);
        return ocKubernetesApplicationInstanceMapper.selectByExample(example);
    }

    @Override
    public OcKubernetesApplicationInstance queryOcKubernetesApplicationInstanceByInstanceName(String instanceName) {
        Example example = new Example(OcKubernetesApplicationInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceName", instanceName);
        return ocKubernetesApplicationInstanceMapper.selectOneByExample(example);
    }

    @Override
    public OcKubernetesApplicationInstance queryOcKubernetesApplicationInstanceById(int id) {
        return ocKubernetesApplicationInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addOcKubernetesApplicationInstance(OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
        ocKubernetesApplicationInstanceMapper.insert(ocKubernetesApplicationInstance);
    }

    @Override
    public void updateOcKubernetesApplicationInstance(OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
        ocKubernetesApplicationInstanceMapper.updateByPrimaryKey(ocKubernetesApplicationInstance);
    }

    @Override
    public void deleteOcKubernetesApplicationInstanceById(int id) {
        ocKubernetesApplicationInstanceMapper.deleteByPrimaryKey(id);
    }
}
