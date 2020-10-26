package com.epam.esm.schepov.service.certificatetag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.service.certificatetag.CertificateTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateTagServiceImpl implements CertificateTagService {

    private final CertificateTagDAO certificateTagDAO;

    @Autowired
    public CertificateTagServiceImpl(CertificateTagDAO certificateTagDAO) {
        this.certificateTagDAO = certificateTagDAO;
    }

    @Override
    public void insertCertificateTag(CertificateTag certificateTag) {
        certificateTagDAO.insert(certificateTag);
    }


    @Override
    public void deleteByCertificateTag(int id) {
        certificateTagDAO.deleteByCertificateId(id);
    }
}
