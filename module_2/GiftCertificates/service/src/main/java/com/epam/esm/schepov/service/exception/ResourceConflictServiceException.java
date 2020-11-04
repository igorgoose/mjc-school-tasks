package com.epam.esm.schepov.service.exception;

public class ResourceConflictServiceException extends ServiceException {
    public ResourceConflictServiceException() {
    }

    public ResourceConflictServiceException(String message) {
        super(message);
    }

    public ResourceConflictServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceConflictServiceException(Throwable cause) {
        super(cause);
    }
}
