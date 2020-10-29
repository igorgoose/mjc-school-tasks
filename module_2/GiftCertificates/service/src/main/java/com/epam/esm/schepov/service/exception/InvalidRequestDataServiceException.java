package com.epam.esm.schepov.service.exception;

public class InvalidRequestDataServiceException extends ServiceException {
    public InvalidRequestDataServiceException() {
    }

    public InvalidRequestDataServiceException(String message) {
        super(message);
    }

    public InvalidRequestDataServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestDataServiceException(Throwable cause) {
        super(cause);
    }
}
