package com.baiyi.caesar.convert;

import com.baiyi.caesar.common.base.CredentialType;
import com.baiyi.caesar.common.util.SSHUtils;
import com.baiyi.caesar.domain.generator.caesar.OcUserCredential;
import com.baiyi.caesar.domain.vo.user.UserCredentialVO;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/2/27 6:27 下午
 * @Version 1.0
 */
public class UserCredentialConvert {


    public static OcUserCredential convertOcUserCredential(UserCredentialVO.UserCredential userCredential) {
        OcUserCredential ocUserCredential = new OcUserCredential();

        ocUserCredential.setCredentialType(userCredential.getCredentialType());
        ocUserCredential.setUserId(userCredential.getUserId());
        ocUserCredential.setUsername(userCredential.getUsername());
        ocUserCredential.setCredential(userCredential.getCredential());
        // 拆分PubKey Title
        if (userCredential.getCredentialType() == CredentialType.SSH_PUB_KEY.getType() && !StringUtils.isEmpty(userCredential.getCredential())) {
            ocUserCredential.setFingerprint(SSHUtils.getFingerprint(userCredential.getCredential())); // 计算指纹
            String credential = userCredential.getCredential();
            String[] content = credential.split(" +");
            if (content.length == 3)
                ocUserCredential.setTitle(content[2]);
        }
        return ocUserCredential;
    }

}
