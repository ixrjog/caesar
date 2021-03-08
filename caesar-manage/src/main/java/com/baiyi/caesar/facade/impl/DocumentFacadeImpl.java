package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.builder.UserDocumentBuilder;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.decorator.document.DocumentDecorator;
import com.baiyi.caesar.domain.BusinessWrapper;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.generator.caesar.OcDocument;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserDocument;
import com.baiyi.caesar.domain.vo.document.DocumentVO;
import com.baiyi.caesar.facade.DocumentFacade;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.service.document.OcDocumentService;
import com.baiyi.caesar.service.document.OcUserDocumentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/12 5:37 下午
 * @Version 1.0
 */
@Service
public class DocumentFacadeImpl implements DocumentFacade {

    @Resource
    private OcDocumentService ocDocumentService;

    @Resource
    private OcUserDocumentService ocUserDocumentService;

    @Resource
    private DocumentDecorator documentDecorator;

    @Resource
    private UserFacade userFacade;

    public static final Integer WORDPAD = 1;

    public static final String DOC_KEY_WORDPAD = "WORDPAD";

    @Override
    public DocumentVO.Doc queryDocByKey(String docKey) {
        OcDocument ocDocument = ocDocumentService.queryOcDocumentByKey(docKey);
        DocumentVO.Doc doc = BeanCopierUtil.copyProperties(ocDocument, DocumentVO.Doc.class);
        return documentDecorator.decorator(doc);
    }

    @Override
    public  DocumentVO.Doc queryDocById(int id){
        OcDocument ocDocument = ocDocumentService.queryOcDocumentById(id);
        DocumentVO.Doc doc = BeanCopierUtil.copyProperties(ocDocument, DocumentVO.Doc.class);
        return documentDecorator.decorator(doc);
    }

    @Override
    public DocumentVO.UserDoc queryUserDocByType(int docType) {
        OcUser ocUser = userFacade.getOcUserBySession();

        OcUserDocument ocUserDocument = ocUserDocumentService.queryOcUserDocumentByUserIdAndType(ocUser.getId(), docType);
        if (ocUserDocument == null) {
            OcDocument ocDocument = ocDocumentService.queryOcDocumentByKey(DOC_KEY_WORDPAD);
            ocUserDocument = UserDocumentBuilder.build(ocUser, ocDocument);
            ocUserDocumentService.addOcUserDocument(ocUserDocument);
        }
        return BeanCopierUtil.copyProperties(ocUserDocument, DocumentVO.UserDoc.class);
    }

    @Override
    public BusinessWrapper<Boolean> saveUserDoc(DocumentVO.UserDoc userDoc) {
        OcUserDocument ocUserDocument = ocUserDocumentService.queryOcUserDocumentById(userDoc.getId());
        if (!ocUserDocument.getUsername().equals(SessionUtil.getUsername()))
            return new BusinessWrapper<>(ErrorEnum.AUTHENTICATION_FAILUER);
        ocUserDocument.setDocContent(userDoc.getDocContent());
        ocUserDocumentService.updateOcUserDocument(ocUserDocument);
        return BusinessWrapper.SUCCESS;
    }

}
