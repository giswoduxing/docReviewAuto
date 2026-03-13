package com.docreview.backend.common.exception;

public class BusinessException extends RuntimeException {

    private final String code;
    private final String messageCode;
    private final transient Object[] messageArguments;

    public BusinessException(String code, String messageCode, Object... messageArguments) {
        super(messageCode);
        this.code = code;
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
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
