package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;

public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final CertificateDAO certificateDAO;

    @Autowired
    public GiftCertificateServiceImpl(CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }




}
