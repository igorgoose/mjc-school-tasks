package com.epam.esm.schepov.service.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.service.certificate.impl.GiftCertificateServiceImpl;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;

import java.util.Set;

/**
 * Provides functionality to process and validate data passed from an endpoint
 * and to send queries to DAO layer.
 *
 * @author Igor Schepov
 * @see GiftCertificateServiceImpl
 * @see GiftCertificate
 * @since 1.0
 */
public interface GiftCertificateService {

    /**
     * Retrieves all certificates stored.
     *
     * @param sortParameter  Certificate's property by which the queried certificates are sorted.
     * @param orderParameter Order of the queried certificates(ascending or descending).
     * @return All certificates stored.
     */
    Set<GiftCertificate> getAllCertificates(String sortParameter, String orderParameter);

    /**
     * Retrieves the certificate with the specified id.
     *
     * @param id The identifier of the queried certificate.
     * @return The certificate with the specified id.
     * @throws ResourceNotFoundServiceException If the queried certificate does not exist.
     */
    GiftCertificate getCertificateById(int id) throws ResourceNotFoundServiceException;

    /**
     * Inserts the passed certificate.
     *
     * @param giftCertificate The certificate to insert.
     * @return The new certificate from storage.
     * @throws ResourceConflictServiceException  If there's a conflict between passed
     *                                           and persisted certificates
     * @throws InvalidEntityDataServiceException If there's an invalid property value
     *                                           inside the passed certificate.
     */
    GiftCertificate insertCertificate(GiftCertificate giftCertificate)
            throws ResourceConflictServiceException, InvalidEntityDataServiceException;

    /**
     * Updates the stored certificate with the specified id using the
     * certificate passed as a parameter.
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
    GiftCertificate updateCertificate(int id, GiftCertificate giftCertificate)
            throws InvalidRequestDataServiceException, InvalidEntityDataServiceException,
            ResourceConflictServiceException;

    /**
     * Deletes the certificate with the specified id from the storage.
     *
     * @param id The id of the certificate to delete.
     * @throws InvalidRequestDataServiceException If the id is invalid.
     */
    void deleteCertificate(int id) throws InvalidRequestDataServiceException;


}
