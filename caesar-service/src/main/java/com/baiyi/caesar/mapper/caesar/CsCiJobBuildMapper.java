package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsCiJobBuild;
import com.baiyi.caesar.domain.vo.dashboard.BuildTaskGroupByHour;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsCiJobBuildMapper extends Mapper<CsCiJobBuild> {

    List<BuildTaskGroupByHour> queryCiJobBuildGroupByHour();
}