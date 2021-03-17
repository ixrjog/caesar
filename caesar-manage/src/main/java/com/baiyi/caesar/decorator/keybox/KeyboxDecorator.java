package com.baiyi.caesar.decorator.keybox;

import com.baiyi.caesar.domain.vo.keybox.KeyboxVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/21 11:31 上午
 * @Version 1.0
 */
@Component
public class KeyboxDecorator {

    public KeyboxVO.Keybox decorator(KeyboxVO.Keybox keybox) {
        keybox.setPassphrase(null);
        keybox.setPublicKey(null);
        keybox.setPrivateKey(null);
        return keybox;
    }

}
