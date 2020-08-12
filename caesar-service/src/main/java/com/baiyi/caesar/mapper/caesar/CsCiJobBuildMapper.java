package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.param.jenkins.JobBuildParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsCiJobBuildMapper extends Mapper<CsCiJobBuild> {

    List<CsCiJobBuild> queryCsCiJobByParam(JobBuildParam.JobBuildPageQuery pageQuery);
}