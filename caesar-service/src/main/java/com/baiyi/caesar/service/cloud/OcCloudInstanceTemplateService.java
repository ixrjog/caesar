package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcCloudInstanceTemplate;
import com.baiyi.caesar.domain.param.cloud.CloudInstanceTemplateParam;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:43 下午
 * @Version 1.0
 */
public interface OcCloudInstanceTemplateService {

    DataTable<OcCloudInstanceTemplate> fuzzyQueryOcCloudInstanceTemplateByParam(CloudInstanceTemplateParam.PageQuery pageQuery);

    void addOcCloudInstanceTemplate(OcCloudInstanceTemplate ocCloudInstanceTemplate);

    void updateOcCloudInstanceTemplate(OcCloudInstanceTemplate ocCloudInstanceTemplate);

    OcCloudInstanceTemplate queryOcCloudInstanceTemplateById(int id);

    void deleteOcCloudInstanceTemplateById(int id);
}
