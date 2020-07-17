package com.baiyi.caesar.gitlab;

import com.baiyi.caesar.domain.generator.caesar.OcAccount;
import com.baiyi.caesar.domain.generator.caesar.OcUser;
import com.baiyi.caesar.domain.vo.user.UserCredentialVO;
import org.gitlab.api.models.GitlabUser;

/**
 * @Author baiyi
 * @Date 2020/1/14 4:16 下午
 * @Version 1.0
 */
public interface GitlabUserCenter {

    GitlabUser createUser(OcUser ocUser, String userDN);

    boolean pushKey(OcUser ocUser, OcAccount ocAccount, UserCredentialVO.UserCredential credential);

}
