package com.baiyi.caesar.common.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/3 5:32 下午
 * @Version 1.0
 */
public class TemplateUtil {

    public static String getTemplate(String templateString, Map<String, String> variable) {
        try {
            StringSubstitutor sub = new StringSubstitutor(variable);
            return sub.replace(templateString);
        } catch (Exception e) {
            return templateString;
        }
    }


}
