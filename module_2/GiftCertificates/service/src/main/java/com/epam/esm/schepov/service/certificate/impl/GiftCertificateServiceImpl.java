package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.persistence.sort.CertificateSortParameter;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

/**
 * Provides functionality to process and validate data passed from an endpoint
 * and to send queries to DAO layer.
 *
 * @author Igor Schepov
 * @see GiftCertificateService
 * @see CertificateTagDAO
 * @see TagDAO
 * @see CertificateDAO
 * @see GiftCertificate
 * @since 1.0
 */
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final CertificateDAO certificateDAO;
    private final TagDAO tagDAO;
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
    public GiftCertificateServiceImpl(CertificateDAO certificateDAO, TagDAO tagDAO,
                                      CertificateTagDAO certificateTagDAO) {
        this.certificateDAO = certificateDAO;
        this.tagDAO = tagDAO;
        this.certificateTagDAO = certificateTagDAO;
    }

    /**
     * Retrieves all certificates stored.
     *
     * @param sortParameter  Certificate's property by which the queried certificates are sorted.
     * @param orderParameter Order of the queried certificates(ascending or descending).
     * @return All certificates stored.
     */
    @Override
    public Set<GiftCertificate> getAllCertificates(String sortParameter, String orderParameter) {
        CertificateSortParameter parameter = Arrays.stream(CertificateSortParameter.values())
                .filter((certificateSortParameter ->
                        certificateSortParameter.getName().equals(sortParameter)))
                .findAny().orElse(null);
        boolean inDescendingOrder = DESCENDING_ORDER.equals(orderParameter);
        if (parameter != null) {
            return certificateDAO.getAll(parameter, inDescendingOrder);
        }
        return certificateDAO.getAll();
    }

    /**
     * Retrieves the certificate with the specified id.
     *
     * @param id The identifier of the queried certificate.
     * @return The certificate with the specified id.
     * @throws ResourceNotFoundServiceException If the queried certificate does not exist.
     */
    @Override
    public GiftCertificate getCertificateById(int id) throws ResourceNotFoundServiceException {
        GiftCertificate certificate = certificateDAO.getById(id);
        if (certificate == null) {
            throw new ResourceNotFoundServiceException("Queried certificate doesn't exist(id=" + id + ")");
        }
        return certificate;
    }

    /**
     * Inserts the passed certificate.
     * <p>
     * The method is {@link Transactional}, so it does not violates database integrity.
     *
     * @param giftCertificate The certificate to insert.
     * @return The new certificate from storage.
     * @throws ResourceConflictServiceException  If there's a conflict between passed
     *                                           and persisted certificates
     * @throws InvalidEntityDataServiceException If there's an invalid property value
     *                                           inside the passed certificate.
     */
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

    /**
     * Deletes the certificate with the specified id from the storage.
     *
     * @param id The id of the certificate to delete.
     * @throws InvalidRequestDataServiceException If the id is invalid.
     */
    @Override
    public void deleteCertificate(int id) throws InvalidRequestDataServiceException {
        if (null == certificateDAO.getById(id)) {
            throw new InvalidRequestDataServiceException("Trying to delete non-existent certificate(id=" + id + ")");
        }
        certificateDAO.delete(id);
    }

    /**
     * Updates the stored certificate with the specified id using the
     * certificate passed as a parameter.
     * <p>
     * The method is {@link Transactional}, so it does not violates database integrity.
     *
     * @param id              The id of the stored certificate to update.
     * @param giftCertificate The updated certificate value.
     * @return The updated certificate from the storage.
     * @throws InvalidRequestDataServiceException If the id is invalid.
     * @throws InvalidEntityDataServiceException  If there's an invalid property value
     *                                            inside the passed certificate.
     * @throws ResourceConflictServiceException   If there's a conflict between passed
     *                                            and persisted certificates
     */
    @Override
    @Transactional
    public GiftCertificate updateCertificate(int id, GiftCertificate giftCertificate)
            throws InvalidRequestDataServiceException, ResourceConflictServiceException,
            InvalidEntityDataServiceException {
        GiftCertificate persistedCertificate = certificateDAO.getByName(giftCertificate.getName());
        if (persistedCertificate != null && persistedCertificate.getId() != id) {
            throw new ResourceConflictServiceException("GiftCertificate with name '"
                    + giftCertificate.getName() + "' already exists.");
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
