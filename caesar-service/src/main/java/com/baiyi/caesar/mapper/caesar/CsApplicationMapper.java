package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsApplication;
import com.baiyi.caesar.domain.param.application.ApplicationParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsApplicationMapper extends Mapper<CsApplication> {

   List<CsApplication> queryCsApplicationByParam(ApplicationParam.ApplicationPageQuery pageQuery);

   List<CsApplication> queryMyCsApplicationByParam(ApplicationParam.MyApplicationPageQuery pageQuery);
}