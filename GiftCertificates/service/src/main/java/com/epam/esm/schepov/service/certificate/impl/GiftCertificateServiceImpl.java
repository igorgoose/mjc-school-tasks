package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.CertificateServiceException;
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
        return certificateDAO.getAll();
    }

    @Override
    public GiftCertificate getCertificateById(int id) throws CertificateServiceException {
        GiftCertificate certificate = certificateDAO.getById(id);
        if (certificate == null) {
            throw new CertificateServiceException("Queried certificate doesn't exist");
        }
        return certificate;
    }

    @Override
    public GiftCertificate insertCertificate(GiftCertificate giftCertificate) throws CertificateServiceException {
        GiftCertificate persistedCertificate = certificateDAO.getByName(giftCertificate.getName());
        if(persistedCertificate != null){
            throw new CertificateServiceException("Certificate with name "
                    + giftCertificate.getName() + " already exists");
        }
        Date now = new Date();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
        certificateDAO.insert(giftCertificate);
        return certificateDAO.getByName(giftCertificate.getName());
    }

    @Override
    public void deleteCertificate(int id) {
        certificateDAO.delete(id);
    }

    @Override
    public void updateCertificate(int id, GiftCertificate giftCertificate) {
        certificateDAO.update(id, giftCertificate);
    }

}
