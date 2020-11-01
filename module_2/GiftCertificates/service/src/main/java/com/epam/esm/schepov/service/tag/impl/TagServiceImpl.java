package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;
    private final CertificateDAO certificateDAO;
    private final CertificateTagDAO certificateTagDAO;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, CertificateDAO certificateDAO, CertificateTagDAO certificateTagDAO) {
        this.tagDAO = tagDAO;
        this.certificateDAO = certificateDAO;
        this.certificateTagDAO = certificateTagDAO;
    }


    @Override
    public Set<Tag> getAllTags() {
        return tagDAO.getAll();
    }

    @Override
    public Tag getTagById(int id) throws ResourceNotFoundServiceException {
        Tag tag = tagDAO.getById(id);
        if (tag == null) {
            throw new ResourceNotFoundServiceException("Queried tag doesn't exist(id=" + id + ")");
        }
        return tag;
    }

    @Override
    public void deleteTagById(int id) throws InvalidRequestDataServiceException {
        if (null == tagDAO.getById(id)) {
            throw new InvalidRequestDataServiceException("Queried tag doesn't exist(id=" + id + ")");
        }
        tagDAO.delete(id);
    }

    @Override
    @Transactional
    public Tag insertTag(Tag tag) throws ResourceConflictServiceException {
        Tag persistedTag = tagDAO.getByName(tag.getName());
        if (persistedTag != null) {
            throw new ResourceConflictServiceException("Tag with name '" + tag.getName() + "' already exists.");
        }
        tagDAO.insert(tag);
        int newTagId = tagDAO.getByName(tag.getName()).getId();
        for (GiftCertificate certificate : tag.getGiftCertificates()){
            GiftCertificate persistedCertificate = ensureGiftCertificateExists(certificate);
            certificateTagDAO.insert(new CertificateTag(persistedCertificate.getId(), newTagId));
        }
        return tagDAO.getByName(tag.getName());
    }

    private GiftCertificate ensureGiftCertificateExists(GiftCertificate giftCertificate){
        GiftCertificate persistedCertificate = certificateDAO.getById(giftCertificate.getId());
        if (persistedCertificate != null) {
            return persistedCertificate;
        }
        throw new InvalidEntityDataServiceException("Invalid certificate id(" + giftCertificate.getId() + ")");
    }

}
