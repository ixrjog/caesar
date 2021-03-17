package com.baiyi.caesar.decorator.user;

import com.baiyi.caesar.common.base.CredentialType;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcUserCredential;
import com.baiyi.caesar.domain.vo.user.UserCredentialVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.user.OcUserCredentialService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/3/17 5:11 下午
 * @Version 1.0
 */
@Component
public class UserCredentialDecorator {

    @Resource
    private OcUserCredentialService ocUserCredentialService;

    public void decorator(UserVO.User user) {
        // 装饰 凭据
        List<OcUserCredential> credentials = ocUserCredentialService.queryOcUserCredentialByUserId(user.getId());
        Map<String, UserCredentialVO.UserCredential> credentialMap = Maps.newHashMap();
        credentials.forEach(e ->
                credentialMap.put(CredentialType.getName(e.getCredentialType()), BeanCopierUtil.copyProperties(e, UserCredentialVO.UserCredential.class))
        );
        user.setCredentialMap(credentialMap);
    }

}
