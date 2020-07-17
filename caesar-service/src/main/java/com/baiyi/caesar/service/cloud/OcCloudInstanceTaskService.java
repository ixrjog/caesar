package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.generator.caesar.OcCloudInstanceTask;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:42 上午
 * @Version 1.0
 */

public interface OcCloudInstanceTaskService {

    void addOcCloudInstanceTask(OcCloudInstanceTask ocCloudInstanceTask);

    void updateOcCloudInstanceTask(OcCloudInstanceTask ocCloudInstanceTask);

    OcCloudInstanceTask queryOcCloudInstanceTaskById(int id);

    OcCloudInstanceTask queryLastOcCloudInstanceTaskByTemplateId(int templateId);
}
