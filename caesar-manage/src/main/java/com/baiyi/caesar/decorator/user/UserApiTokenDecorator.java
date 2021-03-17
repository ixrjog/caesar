package com.baiyi.caesar.decorator.user;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.OcUserApiToken;
import com.baiyi.caesar.domain.vo.user.UserApiTokenVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.user.OcUserApiTokenService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/3/17 5:09 下午
 * @Version 1.0
 */
@Component
public class UserApiTokenDecorator {

    @Resource
    private OcUserApiTokenService ocUserApiTokenService;

    public void decorator(UserVO.User user) {
        // 装饰 ApiToken
        List<OcUserApiToken> userApiTokens = ocUserApiTokenService.queryOcUserApiTokenByUsername(user.getUsername());
        List<UserApiTokenVO.UserApiToken> apiTokens = BeanCopierUtil.copyListProperties(userApiTokens, UserApiTokenVO.UserApiToken.class)
                .stream().peek(e -> e.setToken("申请后不可查看")).collect(Collectors.toList());
        user.setApiTokens(apiTokens);

    }
}
