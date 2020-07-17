package com.baiyi.caesar.builder;

import com.baiyi.caesar.bo.UserDocumentBO;
import com.baiyi.caesar.common.util.BeanCopierUtils;
import com.baiyi.caesar.domain.generator.caesar.OcDocument;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.generator.caesar.OcUserDocument;

/**
 * @Author baiyi
 * @Date 2020/5/13 2:28 下午
 * @Version 1.0
 */
public class UserDocumentBuilder {

    public static OcUserDocument build(OcUser ocUser, OcDocument ocDocument) {
        UserDocumentBO userDocumentBO =  UserDocumentBO.builder()
                .userId(ocUser.getId())
                .username(ocUser.getUsername())
                .comment(ocDocument.getComment())
                .docTitle("用户写字板")
                .docContent(ocDocument.getDocContent())
                .build();
        return covert(userDocumentBO);
    }

    private static OcUserDocument covert(UserDocumentBO userDocumentBO) {
        return BeanCopierUtils.copyProperties(userDocumentBO, OcUserDocument.class);
    }
}
