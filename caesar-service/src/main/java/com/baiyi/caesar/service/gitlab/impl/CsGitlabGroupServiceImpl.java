package com.baiyi.caesar.service.gitlab.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabGroup;
import com.baiyi.caesar.domain.param.gitlab.GitlabGroupParam;
import com.baiyi.caesar.mapper.caesar.CsGitlabGroupMapper;
import com.baiyi.caesar.service.gitlab.CsGitlabGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/10/21 1:37 下午
 * @Version 1.0
 */
@Service
public class CsGitlabGroupServiceImpl implements CsGitlabGroupService {

    @Resource
    private CsGitlabGroupMapper csGitlabGroupMapper;

    @Override
    public DataTable<CsGitlabGroup> queryCsGitlabGroupByParam(GitlabGroupParam.GitlabGroupPageQuery pageQuery) {
        Example example = new Example(CsGitlabGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", pageQuery.getInstanceId());
        if (!StringUtils.isEmpty(pageQuery.getQueryName()))
            criteria.andLike("name", "%" + pageQuery.getQueryName() + "%");
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsGitlabGroup> list = csGitlabGroupMapper.selectByExample(example);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<CsGitlabGroup> queryCsGitlabGroupByInstanceId(Integer instanceId) {
        Example example = new Example(CsGitlabGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return csGitlabGroupMapper.selectByExample(example);
    }

    @Override
    public  CsGitlabGroup  queryGitlabGroupByInstanceIdAndPath(Integer instanceId, String path){
        Example example = new Example(CsGitlabGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        criteria.andEqualTo("path", path);
        return csGitlabGroupMapper.selectOneByExample(example);
    }

    @Override
    public CsGitlabGroup queryCsGitlabGroupByUniqueKey(int instanceId, int groupId){
        Example example = new Example(CsGitlabGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        criteria.andEqualTo("groupId", groupId);
        return csGitlabGroupMapper.selectOneByExample(example);
    }

    @Override
    public int countCsGitlabGroupByInstanceId(Integer instanceId) {
        Example example = new Example(CsGitlabGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return csGitlabGroupMapper.selectCountByExample(example);
    }

    @Override
    public CsGitlabGroup queryCsGitlabGroupById(int id) {
        return csGitlabGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public void addCsGitlabGroup(CsGitlabGroup csGitlabGroup) {
        csGitlabGroupMapper.insert(csGitlabGroup);
    }

    @Override
    public void updateCsGitlabGroup(CsGitlabGroup csGitlabGroup) {
        csGitlabGroupMapper.updateByPrimaryKey(csGitlabGroup);
    }

    @Override
    public void deleteCsGitlabGroupById(int id) {
        csGitlabGroupMapper.deleteByPrimaryKey(id);
    }

}
