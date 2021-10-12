package com.baiyi.caesar.service.aliyun.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsAliyunCrInstance;
import com.baiyi.caesar.domain.param.aliyun.CrParam;
import com.baiyi.caesar.mapper.caesar.CsAliyunCrInstanceMapper;
import com.baiyi.caesar.service.aliyun.CsAliyunCrInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 2:37 下午
 * @Since 1.0
 */

@Service
public class CsAliyunCrInstanceServiceImpl implements CsAliyunCrInstanceService {

    @Resource
    private CsAliyunCrInstanceMapper csAliyunCrInstanceMapper;

    @Override
    public DataTable<CsAliyunCrInstance> csAliyunCrInstancePageQuery(CrParam.InstancePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsAliyunCrInstance> data = csAliyunCrInstanceMapper.csAliyunCrInstancePageQuery(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public CsAliyunCrInstance getById(int id) {
        return csAliyunCrInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public CsAliyunCrInstance getByInstanceId(String instanceId) {
        Example example = new Example(CsAliyunCrInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return csAliyunCrInstanceMapper.selectOneByExample(example);
    }

    @Override
    public void add(CsAliyunCrInstance csAliyunCrInstance) {
        csAliyunCrInstanceMapper.insert(csAliyunCrInstance);
    }

    @Override
    public void update(CsAliyunCrInstance csAliyunCrInstance) {
        csAliyunCrInstanceMapper.updateByPrimaryKey(csAliyunCrInstance);
    }

    @Override
    public void deleteById(int id) {
        csAliyunCrInstanceMapper.deleteByPrimaryKey(id);
    }
}
