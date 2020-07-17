package com.baiyi.caesar.service.cloud;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.OcCloudImage;
import com.baiyi.caesar.domain.param.cloud.CloudImageParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 9:14 上午
 * @Version 1.0
 */
public interface OcCloudImageService {

    List<OcCloudImage> queryOcCloudImageByType(int cloudType);

    void addOcCloudImage(OcCloudImage ocCloudImage);

    void updateOcCloudImage(OcCloudImage ocCloudImage);

    DataTable<OcCloudImage> fuzzyQueryOcCloudImageByParam(CloudImageParam.PageQuery pageQuery);

    OcCloudImage queryOcCloudImageById(int id);

    OcCloudImage queryOcCloudImageByImageId(String imageId);

    void deleteOcCloudImageById(int id);
}
