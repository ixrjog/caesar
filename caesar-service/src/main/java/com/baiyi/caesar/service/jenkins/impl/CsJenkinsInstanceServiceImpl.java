package com.baiyi.caesar.service.jenkins.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsJenkinsInstance;
import com.baiyi.caesar.domain.param.jenkins.JenkinsInstanceParam;
import com.baiyi.caesar.mapper.caesar.CsJenkinsInstanceMapper;
import com.baiyi.caesar.service.jenkins.CsJenkinsInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/17 3:45 下午
 * @Version 1.0
 */
@Service
public class CsJenkinsInstanceServiceImpl implements CsJenkinsInstanceService {

    @Resource
    private CsJenkinsInstanceMapper csJenkinsInstanceMapper;

    @Override
    public List<CsJenkinsInstance> queryAll() {
        return csJenkinsInstanceMapper.selectAll();
    }

    @Override
    public CsJenkinsInstance queryCsJenkinsInstanceById(int id) {
        return csJenkinsInstanceMapper.selectByPrimaryKey(id);
    }


    @Override
    public  CsJenkinsInstance queryCsJenkinsInstanceByName(String name){
        Example example = new Example(CsJenkinsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return csJenkinsInstanceMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<CsJenkinsInstance> queryCsJenkinsInstanceByParam(JenkinsInstanceParam.JenkinsInstancePageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<CsJenkinsInstance> list = csJenkinsInstanceMapper.queryCsJenkinsInstanceByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public void addCsJenkinsInstance(CsJenkinsInstance csJenkinsInstance) {
        csJenkinsInstanceMapper.insert(csJenkinsInstance);
    }

    @Override
    public void updateCsJenkinsInstance(CsJenkinsInstance csJenkinsInstance) {
        csJenkinsInstanceMapper.updateByPrimaryKey(csJenkinsInstance);
    }

    @Override
    public void deleteCsJenkinsInstanceById(int id) {
        csJenkinsInstanceMapper.deleteByPrimaryKey(id);
    }

}
