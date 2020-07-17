package com.baiyi.caesar.service.document;

import com.baiyi.caesar.domain.generator.caesar.OcUserDocument;

/**
 * @Author baiyi
 * @Date 2020/5/13 2:06 下午
 * @Version 1.0
 */
public interface OcUserDocumentService {

    OcUserDocument queryOcUserDocumentByUserIdAndType(int userId, int docType);

    OcUserDocument queryOcUserDocumentById(int id);

    void addOcUserDocument(OcUserDocument ocUserDocument);

    void updateOcUserDocument(OcUserDocument ocUserDocument);
}
