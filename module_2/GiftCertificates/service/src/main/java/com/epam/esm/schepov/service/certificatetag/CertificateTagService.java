package com.epam.esm.schepov.service.certificatetag;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.service.exception.CertificateTagServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;

public interface CertificateTagService {

    void insertCertificateTag(CertificateTag certificateTag) throws ResourceConflictServiceException;

    void deleteByCertificateId(int id) throws ResourceNotFoundServiceException;
}
