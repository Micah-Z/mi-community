package org.mi.common.core.exception;

import org.springframework.http.HttpStatus;

/**
 * @program: mi-community
 * @description:
 * @author: Micah
 * @create: 2020-11-29 17:27
 **/
public class SmsSendFailException extends BaseException {

    private static final long serialVersionUID = 4200588026916757549L;

    public SmsSendFailException(HttpStatus status) {
        super(status);
    }

    public SmsSendFailException(String message) {
        super(500, message);
    }

    public SmsSendFailException(Integer code, String message) {
        super(code, message);
    }
}
