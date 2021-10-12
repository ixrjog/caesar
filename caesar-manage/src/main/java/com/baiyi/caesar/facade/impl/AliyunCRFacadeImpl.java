package com.baiyi.caesar.facade.impl;

import com.aliyuncs.cr.model.v20181201.ListInstanceEndpointResponse;
import com.aliyuncs.cr.model.v20181201.ListInstanceResponse;
import com.baiyi.caesar.aliyun.cr.AliyunCRInstance;
import com.baiyi.caesar.builder.aliyun.AliyunCRInstanceBuilder;
import com.baiyi.caesar.common.base.Global;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.CsAliyunCrInstance;
import com.baiyi.caesar.domain.param.aliyun.CrParam;
import com.baiyi.caesar.domain.vo.aliyun.AliyunCRVO;
import com.baiyi.caesar.facade.AliyunCRFacade;
import com.baiyi.caesar.packer.aliyun.AliyunCRInstancePacker;
import com.baiyi.caesar.service.aliyun.CsAliyunCrInstanceService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/10/11 4:17 下午
 * @Since 1.0
 */

@Component
public class AliyunCRFacadeImpl implements AliyunCRFacade {

    @Resource
    private AliyunCRInstance aliyunCRInstance;

    @Resource
    private CsAliyunCrInstanceService csAliyunCrInstanceService;

    @Resource
    private AliyunCRInstancePacker aliyunCRInstancePacker;

    @Override
    public DataTable<AliyunCRVO.Instance> queryCRInstancePage(CrParam.InstancePageQuery pageQuery) {
        DataTable<CsAliyunCrInstance> table = csAliyunCrInstanceService.csAliyunCrInstancePageQuery(pageQuery);
        List<AliyunCRVO.Instance> page = BeanCopierUtil.copyListProperties(table.getData(), AliyunCRVO.Instance.class);
        return new DataTable<>(page.stream().map(e -> aliyunCRInstancePacker.wrap(e)).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    @Async(value = Global.TaskPools.COMMON)
    public void syncCRInstance() {
        List<ListInstanceResponse.InstancesItem> instancesList = aliyunCRInstance.listInstance();
        instancesList.forEach(instancesItem -> {
            CsAliyunCrInstance oldCsAliyunCrInstance = csAliyunCrInstanceService.getByInstanceId(instancesItem.getInstanceId());
            CsAliyunCrInstance newCsAliyunCrInstance = AliyunCRInstanceBuilder.build(instancesItem);
            ListInstanceEndpointResponse response = aliyunCRInstance.listInstanceEndpoint(instancesItem.getRegionId(), instancesItem.getInstanceId());
            if (response != null) {
                List<ListInstanceEndpointResponse.EndpointsItem> endpointList = response.getEndpoints();
                endpointList.forEach(endpoint -> {
                    ListInstanceEndpointResponse.EndpointsItem.DomainsItem domains = endpoint.getDomains().get(0);
                    switch (endpoint.getEndpointType()) {
                        case "internet":
                            newCsAliyunCrInstance.setInternetEndpoint(domains.getDomain());
                            break;
                        case "intranet":
                            newCsAliyunCrInstance.setIntranetEndpoint(domains.getDomain());
                            break;
                        case "vpc":
                            newCsAliyunCrInstance.setVpcEndpoint(domains.getDomain());
                            break;
                        default:
                    }
                });
            }
            if (oldCsAliyunCrInstance == null) {
                csAliyunCrInstanceService.add(newCsAliyunCrInstance);
            } else {
                newCsAliyunCrInstance.setId(oldCsAliyunCrInstance.getId());
                csAliyunCrInstanceService.update(newCsAliyunCrInstance);
            }
        });
    }

    @Override
    public BusinessWrapper<Boolean> deleteInstanceById(int id) {
        csAliyunCrInstanceService.deleteById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> setInstanceActiveById(int id) {
        CsAliyunCrInstance csAliyunCrInstance = csAliyunCrInstanceService.getById(id);
        csAliyunCrInstance.setIsActive(!csAliyunCrInstance.getIsActive());
        csAliyunCrInstanceService.update(csAliyunCrInstance);
        return BusinessWrapper.SUCCESS;
    }
}
