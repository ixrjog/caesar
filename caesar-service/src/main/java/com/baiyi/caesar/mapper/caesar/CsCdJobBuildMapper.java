package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsCdJobBuild;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGroupByHour;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsCdJobBuildMapper extends Mapper<CsCdJobBuild> {

    List<BuildTaskGroupByHour> queryCdJobBuildGroupByHour();
}