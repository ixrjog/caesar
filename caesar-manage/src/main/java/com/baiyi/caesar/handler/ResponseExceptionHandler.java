package com.baiyi.caesar.handler;

import com.baiyi.caesar.common.exception.build.BuildRuntimeException;
import com.baiyi.caesar.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(value = {BuildRuntimeException.class})
    public HttpResult handleRuntimeException(BuildRuntimeException exception) {
        return new HttpResult(exception.getCode(), exception.getMessage());
    }

}
