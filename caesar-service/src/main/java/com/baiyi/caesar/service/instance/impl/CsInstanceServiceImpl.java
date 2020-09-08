package com.baiyi.caesar.service.instance.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsInstance;
import com.baiyi.caesar.domain.param.caesar.CaesarInstanceParam;
import com.baiyi.caesar.mapper.caesar.CsInstanceMapper;
import com.baiyi.caesar.service.instance.CsInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/9/7 11:01 上午
 * @Version 1.0
 */
@Service
public class CsInstanceServiceImpl implements CsInstanceService {

    @Resource
    private CsInstanceMapper csInstanceMapper;

    @Override
    public DataTable<CsInstance> queryCsInstanceByParam(CaesarInstanceParam.CaesarInstancePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsInstance> list = csInstanceMapper.queryCsInstanceByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }


    @Override
    public void addCsInstance(CsInstance csInstance) {
        csInstanceMapper.insert(csInstance);
    }

    @Override
    public void updateCsInstance(CsInstance csInstance) {
        csInstanceMapper.updateByPrimaryKey(csInstance);
    }

    @Override
    public CsInstance queryCsInstanceByHostIp(String hostIp) {
        Example example = new Example(CsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("hostIp", hostIp);
        return csInstanceMapper.selectOneByExample(example);
    }

    @Override
    public CsInstance queryCsInstanceById(int id) {
        return csInstanceMapper.selectByPrimaryKey(id);
    }

}
