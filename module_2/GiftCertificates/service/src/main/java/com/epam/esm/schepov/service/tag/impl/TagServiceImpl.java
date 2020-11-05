package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.persistence.sort.TagSortParameter;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;


/**
 * Provides functionality to process and validate data passed from an endpoint
 * and to send queries to DAO layer.
 *
 * @author Igor Schepov
 * @see TagService
 * @see CertificateTagDAO
 * @see TagDAO
 * @see CertificateDAO
 * @see Tag
 * @since 1.0
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;
    private final CertificateDAO certificateDAO;
    private final CertificateTagDAO certificateTagDAO;
    private static final String DESCENDING_ORDER = "desc";

    /**
     * Injects objects of classes implementing {@link CertificateDAO}, {@link TagDAO} and
     * {@link CertificateTagDAO} used to communicate with the database.
     *
     * @param certificateDAO    Certificate Data Access Object.
     * @param tagDAO            Tag Data Access Object.
     * @param certificateTagDAO CertificateTag Data Access Object.
     */
    @Autowired
    public TagServiceImpl(TagDAO tagDAO, CertificateDAO certificateDAO, CertificateTagDAO certificateTagDAO) {
        this.tagDAO = tagDAO;
        this.certificateDAO = certificateDAO;
        this.certificateTagDAO = certificateTagDAO;
    }


    /**
     * Retrieves all tags stored.
     *
     * @param sortParameter  Tags's property by which the queried tags are sorted.
     * @param orderParameter Order of the queried tags(ascending or descending).
     * @return All tags stored.
     */
    @Override
    public Set<Tag> getAllTags(String sortParameter, String orderParameter) {
        TagSortParameter parameter = Arrays.stream(TagSortParameter.values())
                .filter((certificateSortParameter ->
                        certificateSortParameter.getName().equals(sortParameter)))
                .findAny().orElse(null);
        boolean inDescendingOrder = DESCENDING_ORDER.equals(orderParameter);
        if (parameter != null) {
            return tagDAO.getAll(parameter, inDescendingOrder);
        }
        return tagDAO.getAll();
    }

    /**
     * Retrieves the tag with the specified id.
     *
     * @param id The identifier of the queried tag.
     * @return The tag with the specified id.
     * @throws ResourceNotFoundServiceException If the queried tag does not exist.
     */
    @Override
    public Tag getTagById(int id) throws ResourceNotFoundServiceException {
        Tag tag = tagDAO.getById(id);
        if (tag == null) {
            throw new ResourceNotFoundServiceException("Queried tag doesn't exist(id=" + id + ")");
        }
        return tag;
    }

    /**
     * Deletes the certificate with the specified id from the storage.
     *
     * @param id The id of the certificate to delete.
     * @throws InvalidRequestDataServiceException If the id is invalid.
     */
    @Override
    public void deleteTagById(int id) throws InvalidRequestDataServiceException {
        if (null == tagDAO.getById(id)) {
            throw new InvalidRequestDataServiceException("Queried tag doesn't exist(id=" + id + ")");
        }
        tagDAO.delete(id);
    }

    /**
     * Inserts the passed certificate.
     * <p>
     * The method is {@link Transactional}, so it does not violates database integrity.
     *
     * @param tag The tag to insert.
     * @return The new tag from storage.
     * @throws ResourceConflictServiceException  If there's a conflict between passed
     *                                           and persisted tags.
     * @throws InvalidEntityDataServiceException If there's an invalid property value
     *                                           inside the passed tag.
     */
    @Override
    @Transactional
    public Tag insertTag(Tag tag) throws ResourceConflictServiceException,
            InvalidEntityDataServiceException {
        Tag persistedTag = tagDAO.getByName(tag.getName());
        if (persistedTag != null) {
            throw new ResourceConflictServiceException("Tag with name '" + tag.getName() + "' already exists.");
        }
        tagDAO.insert(tag);
        int newTagId = tagDAO.getByName(tag.getName()).getId();
        for (GiftCertificate certificate : tag.getGiftCertificates()) {
            GiftCertificate persistedCertificate = ensureGiftCertificateExists(certificate);
            certificateTagDAO.insert(new CertificateTag(persistedCertificate.getId(), newTagId));
        }
        return tagDAO.getByName(tag.getName());
    }

    private GiftCertificate ensureGiftCertificateExists(GiftCertificate giftCertificate)
            throws InvalidEntityDataServiceException {
        GiftCertificate persistedCertificate = certificateDAO.getById(giftCertificate.getId());
        if (persistedCertificate != null) {
            return persistedCertificate;
        }
        throw new InvalidEntityDataServiceException("Invalid certificate id(" + giftCertificate.getId() + ")");
    }

}
