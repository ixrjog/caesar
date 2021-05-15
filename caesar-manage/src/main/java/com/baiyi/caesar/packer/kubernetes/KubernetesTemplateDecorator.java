package com.baiyi.caesar.packer.kubernetes;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.KubernetesUtil;
import com.baiyi.caesar.common.util.TemplateUtil;
import com.baiyi.caesar.domain.generator.caesar.OcEnv;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplication;
import com.baiyi.caesar.domain.generator.caesar.OcKubernetesApplicationInstance;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.kubernetes.KubernetesTemplateVO;
import com.baiyi.caesar.domain.vo.kubernetes.templateVariable.TemplateVariable;
import com.baiyi.caesar.service.env.OcEnvService;
import com.baiyi.caesar.service.kubernetes.OcKubernetesApplicationService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/30 2:07 下午
 * @Version 1.0
 */
@Component
public class KubernetesTemplateDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcKubernetesApplicationService ocKubernetesApplicationService;

    public KubernetesTemplateVO.Template decorator(KubernetesTemplateVO.Template template, OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
        template = decorator(template);
        if (ocKubernetesApplicationInstance != null) {
            Map<String, String> variable = Maps.newHashMap();
            TemplateVariable templateVariable = KubernetesUtil.buildVariable(ocKubernetesApplicationInstance.getTemplateVariable());
            if (templateVariable != null)
                variable.putAll(templateVariable.getVariable());
            variable.put("envLabel", ocKubernetesApplicationInstance.getEnvLabel());
            OcKubernetesApplication ocKubernetesApplication = ocKubernetesApplicationService.queryOcKubernetesApplicationById(ocKubernetesApplicationInstance.getApplicationId());
            variable.put("appName", ocKubernetesApplication.getName());

            template.setTemplateYaml(TemplateUtil.getTemplate(template.getTemplateYaml(), variable));
        }
        return template;
    }

    public KubernetesTemplateVO.Template decorator(KubernetesTemplateVO.Template template) {
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(template.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtil.copyProperties(ocEnv, EnvVO.Env.class);
            template.setEnv(env);
        }
        return template;
    }
}
