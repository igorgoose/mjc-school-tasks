package com.epam.esm.schepov.error;

public class Error {

    private final String errorMessage;
    private final int errorCode;

    public Error(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
