package com.epam.esm.schepov.service.certificatetag;

import com.epam.esm.schepov.core.entity.CertificateTag;

public interface CertificateTagService {

    void insertCertificateTag(CertificateTag certificateTag);

    void deleteByCertificateTag(int id);
}
