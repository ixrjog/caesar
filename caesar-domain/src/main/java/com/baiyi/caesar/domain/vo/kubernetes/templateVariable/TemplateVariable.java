package com.baiyi.caesar.domain.vo.kubernetes.templateVariable;

import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/3 4:30 下午
 * @Version 1.0
 */
@Data
public class TemplateVariable {

    private Map<String,String> variable;
}
