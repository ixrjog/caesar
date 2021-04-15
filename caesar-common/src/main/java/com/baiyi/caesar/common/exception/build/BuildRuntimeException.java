package com.baiyi.caesar.common.exception.build;

import com.baiyi.caesar.domain.ErrorEnum;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/26 4:45 下午
 * @Since 1.0
 */
@Data
public class BuildRuntimeException extends RuntimeException {

    private Integer code;

    public BuildRuntimeException(String message) {
        super(message);
        this.code = 999;
    }

    public BuildRuntimeException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BuildRuntimeException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }
}
