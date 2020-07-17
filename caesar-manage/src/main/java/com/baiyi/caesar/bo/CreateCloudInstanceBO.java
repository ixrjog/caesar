package com.baiyi.caesar.bo;

import com.baiyi.caesar.domain.generator.caesar.OcCloudImage;
import com.baiyi.caesar.domain.generator.caesar.OcCloudVpcSecurityGroup;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.caesar.domain.vo.cloud.CloudInstanceTemplateVO;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/3/30 11:12 上午
 * @Version 1.0
 */
@Data
@Builder
public class CreateCloudInstanceBO{

   private CloudInstanceTemplateParam.CreateCloudInstance createCloudInstance;
   private CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate;
   private OcCloudImage ocCloudImage;
   private OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup;
   private OcServerGroup ocServerGroup;

}
