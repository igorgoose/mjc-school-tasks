package com.epam.esm.schepov.service.exception;

public class CertificateTagServiceException extends ServiceException {
    public CertificateTagServiceException() {
    }

    public CertificateTagServiceException(String message) {
        super(message);
    }

    public CertificateTagServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificateTagServiceException(Throwable cause) {
        super(cause);
    }
}
