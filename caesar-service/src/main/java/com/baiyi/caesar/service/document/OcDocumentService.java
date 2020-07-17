package com.baiyi.caesar.service.document;

import com.baiyi.caesar.domain.generator.caesar.OcDocument;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:38 下午
 * @Version 1.0
 */
public interface OcDocumentService {

    OcDocument queryOcDocumentByKey(String docKey);

    OcDocument queryOcDocumentById(int id);
}
