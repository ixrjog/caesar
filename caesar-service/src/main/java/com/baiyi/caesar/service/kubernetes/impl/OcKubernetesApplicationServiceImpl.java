package com.baiyi.caesar.service.kubernetes.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplication;
import com.baiyi.caesar.domain.param.kubernetes.KubernetesApplicationParam;
import com.baiyi.caesar.mapper.caesar.OcKubernetesApplicationMapper;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 6:13 下午
 * @Version 1.0
 */
@Service
public class OcKubernetesApplicationServiceImpl implements OcKubernetesApplicationService {

    @Resource
    private OcKubernetesApplicationMapper ocKubernetesApplicationMapper;

    @Override
    public DataTable<OcKubernetesApplication> queryOcKubernetesApplicationByParam(KubernetesApplicationParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcKubernetesApplication> list = ocKubernetesApplicationMapper.queryOcKubernetesApplicationByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication) {
        ocKubernetesApplicationMapper.insert(ocKubernetesApplication);
    }

    @Override
    public void updateOcKubernetesApplication(OcKubernetesApplication ocKubernetesApplication) {
        ocKubernetesApplicationMapper.updateByPrimaryKey(ocKubernetesApplication);
    }

    @Override
    public void deleteOcKubernetesApplicationById(int id) {
        ocKubernetesApplicationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OcKubernetesApplication queryOcKubernetesApplicationById(int id) {
        return ocKubernetesApplicationMapper.selectByPrimaryKey(id);
    }
}
