package com.baiyi.caesar.common.exception.auth;

import com.baiyi.caesar.domain.ErrorEnum;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/5/8 3:07 下午
 * @Version 1.0
 */
@Data
public class AuthenticationRuntimeException extends RuntimeException {

    private Integer code;

    public AuthenticationRuntimeException(String message) {
        super(message);
        this.code = 999;
    }

    public AuthenticationRuntimeException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthenticationRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }
}
