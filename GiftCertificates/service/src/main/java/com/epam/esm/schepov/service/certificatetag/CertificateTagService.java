package com.epam.esm.schepov.service.certificatetag;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.service.exception.CertificateTagServiceException;

public interface CertificateTagService {

    void insertCertificateTag(CertificateTag certificateTag) throws CertificateTagServiceException;

    void deleteByCertificateId(int id) throws CertificateTagServiceException;
}
