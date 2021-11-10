package com.baiyi.caesar.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class HttpResult<T> implements Serializable {

    public static final HttpResult<Boolean> SUCCESS = new HttpResult<>(true);

    private static final long serialVersionUID = 433215564439560523L;

    private T body;

    private boolean success;

    private String msg;

    private int code;

    public HttpResult(T body) {
        this.body = body;
        this.success = true;
    }

    public HttpResult(BusinessWrapper<T> wrapper) {
        this.success = wrapper.isSuccess();
        if (wrapper.isSuccess()) {
            this.body = wrapper.getBody();
        } else {
            this.code = wrapper.getCode();
            this.msg = wrapper.getDesc();
        }
    }

    public HttpResult(ErrorEnum errorEnum) {
        this.success = false;
        this.code = errorEnum.getCode();
        this.msg = errorEnum.getMessage();
    }

    public HttpResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.success = false;
    }
}
