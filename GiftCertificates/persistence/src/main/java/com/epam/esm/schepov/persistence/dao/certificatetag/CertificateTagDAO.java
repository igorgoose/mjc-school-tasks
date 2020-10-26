package com.epam.esm.schepov.persistence.dao.certificatetag;

import com.epam.esm.schepov.core.entity.CertificateTag;

public interface CertificateTagDAO {
    CertificateTag getById(int id);

    void insert(CertificateTag certificateTag);

    CertificateTag getByCertificateIdAndTagId(int certificateId, int tagId);

    void deleteByCertificateId(int id);
}
