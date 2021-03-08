package com.baiyi.caesar.decorator.document;

import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.common.util.TemplateUtil;
import com.baiyi.caesar.domain.vo.document.DocumentVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:51 下午
 * @Version 1.0
 */
@Component
public class DocumentDecorator {

    public DocumentVO.Doc decorator(DocumentVO.Doc doc) {
        Map<String, String> valuesMap = Maps.newHashMap();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("username", SessionUtil.getUsername());
        doc.setPreviewDoc(TemplateUtil.getTemplate(doc.getDocContent(), valuesMap));
        doc.setDocContent("");
        return doc;
    }

}
