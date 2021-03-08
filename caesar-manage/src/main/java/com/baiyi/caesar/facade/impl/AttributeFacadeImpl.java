package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.ansible.config.AnsibleConfig;
import com.baiyi.caesar.common.util.IOUtil;
import com.baiyi.caesar.domain.generator.caesar.OcServerGroup;
import com.baiyi.caesar.domain.vo.server.PreviewAttributeVO;
import com.baiyi.caesar.facade.AttributeFacade;
import com.baiyi.caesar.factory.attribute.impl.AttributeAnsible;
import com.baiyi.caesar.service.server.OcServerGroupService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.caesar.ansible.config.AnsibleConfig.ANSIBLE_HOSTS;

/**
 * @Author baiyi
 * @Date 2020/4/10 11:28 上午
 * @Version 1.0
 */
@Component
public class AttributeFacadeImpl implements AttributeFacade {

    @Resource
    private AttributeAnsible attributeAnsible;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private AnsibleConfig ansibleConfig;

    @Override
    public void createAnsibleHostsTask() {
        try {
            StringBuilder context = new StringBuilder(attributeAnsible.getHeadInfo());
            List<OcServerGroup> serverGroupList = ocServerGroupService.queryAll();
            for (OcServerGroup serverGroup : serverGroupList) {
                PreviewAttributeVO.PreviewAttribute previewAttribute = attributeAnsible.build(serverGroup);
                if (!StringUtils.isEmpty(previewAttribute.getContent()))
                    context.append("\n").append(previewAttribute.getContent());
            }
            IOUtil.createFile(ansibleConfig.acqInventoryPath(), ANSIBLE_HOSTS, context.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
