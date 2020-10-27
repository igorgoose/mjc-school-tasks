package com.epam.esm.schepov.service.exception;

public class CertificateServiceException extends ServiceException{
    public CertificateServiceException() {
    }

    public CertificateServiceException(String message) {
        super(message);
    }

    public CertificateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateServiceException(Throwable cause) {
        super(cause);
    }
}
