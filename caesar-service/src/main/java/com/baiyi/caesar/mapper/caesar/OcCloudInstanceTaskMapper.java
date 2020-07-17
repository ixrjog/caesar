package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.OcCloudInstanceTask;
import tk.mybatis.mapper.common.Mapper;

public interface OcCloudInstanceTaskMapper extends Mapper<OcCloudInstanceTask> {

    OcCloudInstanceTask queryLastOcCloudInstanceTaskByTemplateId(int templateId);
}