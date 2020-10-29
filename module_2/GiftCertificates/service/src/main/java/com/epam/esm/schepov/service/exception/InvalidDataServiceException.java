package com.epam.esm.schepov.service.exception;

public class InvalidDataServiceException extends RuntimeException{
    public InvalidDataServiceException() {
    }

    public InvalidDataServiceException(String message) {
        super(message);
    }

    public InvalidDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataServiceException(Throwable cause) {
        super(cause);
    }
}
