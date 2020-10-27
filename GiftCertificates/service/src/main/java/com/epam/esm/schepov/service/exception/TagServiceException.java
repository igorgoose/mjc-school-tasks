package com.epam.esm.schepov.service.exception;

public class TagServiceException extends ServiceException {

    public TagServiceException() {
    }

    public TagServiceException(String message) {
        super(message);
    }

    public TagServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagServiceException(Throwable cause) {
        super(cause);
    }
}
