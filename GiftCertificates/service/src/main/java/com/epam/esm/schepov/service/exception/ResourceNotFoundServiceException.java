package com.epam.esm.schepov.service.exception;

public class ResourceNotFoundServiceException extends ServiceException{
    public ResourceNotFoundServiceException() {
    }

    public ResourceNotFoundServiceException(String message) {
        super(message);
    }

    public ResourceNotFoundServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundServiceException(Throwable cause) {
        super(cause);
    }
}
