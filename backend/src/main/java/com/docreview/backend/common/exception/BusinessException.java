package com.docreview.backend.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;
    private final String messageCode;
    private final transient Object[] messageArguments;

    public BusinessException(String code, String messageCode, Object... messageArguments) {
        this(HttpStatus.BAD_REQUEST, code, messageCode, messageArguments);
    }

    public BusinessException(
        HttpStatus httpStatus,
        String code,
        String messageCode,
        Object... messageArguments
    ) {
        super(messageCode);
        this.httpStatus = httpStatus;
        this.code = code;
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Object[] getMessageArguments() {
        return messageArguments;
    }
}
