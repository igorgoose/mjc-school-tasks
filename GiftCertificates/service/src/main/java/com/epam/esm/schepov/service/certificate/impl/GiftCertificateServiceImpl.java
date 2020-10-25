package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final CertificateDAO certificateDAO;

    @Autowired
    public GiftCertificateServiceImpl(CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }


    @Override
    public Set<GiftCertificate> getAllCertificates() {
        return certificateDAO.getAllCertificates();
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        return certificateDAO.getCertificateById(id);
    }

    @Override
    public void insertCertificate(GiftCertificate giftCertificate) {
        //todo validation
        Date now = new Date();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
        certificateDAO.insertCertificate(giftCertificate);
    }

    @Override
    public void deleteCertificate(int id) {
        certificateDAO.deleteCertificate(id);
    }

}
