package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudDbDatabase;
import com.baiyi.caesar.domain.param.cloud.CloudDBDatabaseParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudDbDatabaseMapper extends Mapper<OcCloudDbDatabase> {

    List<OcCloudDbDatabase> fuzzyQueryOcCloudDbDatabaseByParam(CloudDBDatabaseParam.PageQuery pageQuery);

    int updateBaseOcCloudDbDatabase(OcCloudDbDatabase ocCloudDbDatabase);
}