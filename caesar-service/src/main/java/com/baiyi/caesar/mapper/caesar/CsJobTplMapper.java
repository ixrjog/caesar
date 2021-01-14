package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsJobTpl;
import com.baiyi.caesar.domain.param.jenkins.JobTplParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsJobTplMapper extends Mapper<CsJobTpl> {

    List<CsJobTpl> queryCsJobTplByParam(JobTplParam.JobTplPageQuery pageQuery);

}