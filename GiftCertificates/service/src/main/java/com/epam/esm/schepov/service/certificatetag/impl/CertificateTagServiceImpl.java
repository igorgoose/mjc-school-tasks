package com.epam.esm.schepov.service.certificatetag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.service.certificatetag.CertificateTagService;
import com.epam.esm.schepov.service.exception.CertificateTagServiceException;
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
    public void insertCertificateTag(CertificateTag certificateTag) throws CertificateTagServiceException {
        CertificateTag persistedCertificateTag = certificateTagDAO.
                getByCertificateIdAndTagId(certificateTag.getCertificateId(), certificateTag.getTagId());
        if(persistedCertificateTag != null){
            throw new CertificateTagServiceException("CertificateTag with certificate id = "
                    + certificateTag.getCertificateId() + " and tag id = " + certificateTag.getTagId() +
                    " already exists.");
        }
        certificateTagDAO.insert(certificateTag);
    }

    @Override
    public void deleteByCertificateId(int id) throws CertificateTagServiceException {
        try {
            certificateTagDAO.deleteByCertificateId(id);
        } catch (DaoException exception){
            throw new CertificateTagServiceException(exception);
        }
    }

}
