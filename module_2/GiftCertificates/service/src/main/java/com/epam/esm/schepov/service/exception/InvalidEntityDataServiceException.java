package com.epam.esm.schepov.service.exception;

public class InvalidEntityDataServiceException extends RuntimeException{
    public InvalidEntityDataServiceException() {
    }

    public InvalidEntityDataServiceException(String message) {
        super(message);
    }

    public InvalidEntityDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEntityDataServiceException(Throwable cause) {
        super(cause);
    }
}
