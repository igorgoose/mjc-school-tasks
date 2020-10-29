package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final CertificateDAO certificateDAO;
    private final TagDAO tagDAO;
    private final CertificateTagDAO certificateTagDAO;

    @Autowired
    public GiftCertificateServiceImpl(CertificateDAO certificateDAO, TagDAO tagDAO,
                                      CertificateTagDAO certificateTagDAO) {
        this.certificateDAO = certificateDAO;
        this.tagDAO = tagDAO;
        this.certificateTagDAO = certificateTagDAO;
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
    @Transactional
    public GiftCertificate insertCertificate(GiftCertificate giftCertificate)
            throws ResourceConflictServiceException, InvalidEntityDataServiceException {
        GiftCertificate persistedCertificate = certificateDAO.getByName(giftCertificate.getName());
        if (persistedCertificate != null) {
            throw new ResourceConflictServiceException("Certificate with name '" + giftCertificate.getName()
                    + "' already exists.");
        }
        Date now = new Date();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
        certificateDAO.insert(giftCertificate);
        int newCertificateId = certificateDAO.getByName(giftCertificate.getName()).getId();
        for (Tag tag : giftCertificate.getTags()) {
            Tag persistedTag = ensureTagExists(tag);
            certificateTagDAO.insert(new CertificateTag(newCertificateId, persistedTag.getId()));
        }
        return certificateDAO.getByName(giftCertificate.getName());
    }

    @Override
    public void deleteCertificate(int id) throws InvalidRequestDataServiceException {
        if (null == certificateDAO.getById(id)) {
            throw new InvalidRequestDataServiceException("Trying to delete non-existent certificate(id=" + id + ")");
        }
        certificateDAO.delete(id);
    }

    @Override
    @Transactional
    public GiftCertificate updateCertificate(int id, GiftCertificate giftCertificate)
            throws InvalidRequestDataServiceException, ResourceConflictServiceException,
            InvalidEntityDataServiceException {
        GiftCertificate persistedCertificate = certificateDAO.getByName(giftCertificate.getName());
        if (persistedCertificate != null && persistedCertificate.getId() != id) {
            throw new ResourceConflictServiceException("GiftCertificate with name "
                    + giftCertificate.getName() + " already exists.");
        }
        persistedCertificate = certificateDAO.getById(id);
        if (persistedCertificate == null) {
            throw new InvalidRequestDataServiceException("Trying to update non-existent certificate(id=" + id + ")");
        }
        certificateTagDAO.deleteByCertificateId(id);
        for (Tag tagToAdd : giftCertificate.getTags()) {
            Tag persistedTag = ensureTagExists(tagToAdd);
            certificateTagDAO.insert(new CertificateTag(id, persistedTag.getId()));
        }
        certificateDAO.update(id, giftCertificate);
        return certificateDAO.getById(id);
    }

    private Tag ensureTagExists(Tag tag) throws InvalidEntityDataServiceException {
        Tag persistedTag = tagDAO.getById(tag.getId());
        if (persistedTag != null) {
            return persistedTag;
        }
        throw new InvalidEntityDataServiceException("Invalid tag id(" + tag.getId() + ")");
    }
}
