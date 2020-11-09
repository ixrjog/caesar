package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsCiJob;
import com.baiyi.caesar.domain.param.application.CiJobParam;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGoupByWeek;
import com.baiyi.caesar.domain.vo.dashboard.JobTypeTotal;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsCiJobMapper extends Mapper<CsCiJob> {

    List<CsCiJob> queryCsCiJobByParam(CiJobParam.CiJobPageQuery pageQuery);

    List<CsCiJob> queryCsCiJobTplByParam( CiJobParam.CiJobTplPageQuery pageQuery);

    List<JobTypeTotal> queryJobTypeTotal();

    List<BuildTaskGoupByWeek> queryBuildTaskGoupByWeek();
}