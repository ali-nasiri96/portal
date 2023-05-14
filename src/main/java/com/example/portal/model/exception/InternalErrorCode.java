package com.example.portal.model.exception;

public enum InternalErrorCode {
    USER_ALREADY_EXIST("user.already.exist");

    private final String errorKey;


    InternalErrorCode(String errorKey) {
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
