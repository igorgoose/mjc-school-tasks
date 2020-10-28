package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
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
    public GiftCertificate getCertificateById(int id) throws ResourceNotFoundServiceException {
        GiftCertificate certificate = certificateDAO.getById(id);
        if (certificate == null) {
            throw new ResourceNotFoundServiceException("Queried certificate doesn't exist(id=" + id + ")");
        }
        return certificate;
    }

    @Override
    public GiftCertificate insertCertificate(GiftCertificate giftCertificate) throws ResourceConflictServiceException {
        GiftCertificate persistedCertificate = certificateDAO.getByName(giftCertificate.getName());
        if(persistedCertificate != null){
            throw new ResourceConflictServiceException("Created certificate already exists(name="
                    + giftCertificate.getName() + ")");
        }
        Date now = new Date();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
        certificateDAO.insert(giftCertificate);
        return certificateDAO.getByName(giftCertificate.getName());
    }

    @Override
    public void deleteCertificate(int id) throws ResourceNotFoundServiceException {
        getCertificateById(id);
        certificateDAO.delete(id);
    }

    @Override
    public GiftCertificate updateCertificate(int id, GiftCertificate giftCertificate)
            throws ResourceNotFoundServiceException {
        GiftCertificate persistedCertificate = certificateDAO.getByName(giftCertificate.getName());
        if(persistedCertificate != null && persistedCertificate.getId() != id){
            throw new ResourceConflictServiceException("GiftCertificate with name "
                    + giftCertificate.getName() + " already exists.");
        }
        persistedCertificate = getCertificateById(id);
        if(persistedCertificate == null){
            throw new ResourceNotFoundServiceException("Queried certificate doesn't exist(id=" + id + ")");
        }
        certificateDAO.update(id, giftCertificate);
        giftCertificate.setId(id);
        return giftCertificate;
    }

}
