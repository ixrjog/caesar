package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.CsDingtalk;
import com.baiyi.caesar.domain.param.dingtalk.DingtalkParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CsDingtalkMapper extends Mapper<CsDingtalk> {

    List<CsDingtalk> queryCsDingtalkByParam(DingtalkParam.DingtalkPageQuery pageQuery);
}