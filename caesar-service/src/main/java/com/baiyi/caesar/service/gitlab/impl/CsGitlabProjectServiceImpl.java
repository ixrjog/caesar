package com.baiyi.caesar.service.gitlab.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsGitlabProject;
import com.baiyi.caesar.domain.param.gitlab.GitlabProjectParam;
import com.baiyi.caesar.mapper.caesar.CsGitlabProjectMapper;
import com.baiyi.caesar.service.gitlab.CsGitlabProjectService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/20 11:03 上午
 * @Version 1.0
 */
@Service
public class CsGitlabProjectServiceImpl implements CsGitlabProjectService {

    @Resource
    private CsGitlabProjectMapper csGitlabProjectMapper;

    @Override
    public  DataTable<CsGitlabProject> queryCsGitlabProjectByParam(GitlabProjectParam.GitlabProjectPageQuery pageQuery){
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CsGitlabProject> list = csGitlabProjectMapper.queryCsGitlabProjectByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public List<CsGitlabProject> queryCsGitlabProjectByInstanceId(Integer instanceId) {
        Example example = new Example(CsGitlabProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return csGitlabProjectMapper.selectByExample(example);
    }

    @Override
    public List<CsGitlabProject> queryCsGitlabProjectByInstanceIdAndNamespacePath(Integer instanceId,String namespacePath) {
        Example example = new Example(CsGitlabProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        criteria.andEqualTo("namespacePath",namespacePath);
        return csGitlabProjectMapper.selectByExample(example);
    }


    @Override
    public int countCsGitlabProjectByInstanceId(Integer instanceId) {
        Example example = new Example(CsGitlabProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return csGitlabProjectMapper.selectCountByExample(example);
    }

    @Override
    public CsGitlabProject queryCsGitlabProjectById(int id) {
        return csGitlabProjectMapper.selectByPrimaryKey(id);
    }

    @Override
    public CsGitlabProject queryCsGitlabProjectByUniqueKey(int instanceId, int projectId){
        Example example = new Example(CsGitlabProject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        criteria.andEqualTo("projectId", projectId);
        return csGitlabProjectMapper.selectOneByExample(example);
    }

    @Override
    public void addCsGitlabProject(CsGitlabProject csGitlabProject) {
        csGitlabProjectMapper.insert(csGitlabProject);
    }

    @Override
    public void updateCsGitlabProject(CsGitlabProject csGitlabProject) {
        csGitlabProjectMapper.updateByPrimaryKey(csGitlabProject);
    }

    @Override
    public void deleteCsGitlabProjectById(int id) {
        csGitlabProjectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int countAllCsGitlabProject(){
       return csGitlabProjectMapper.selectCount(null);
    }
}
