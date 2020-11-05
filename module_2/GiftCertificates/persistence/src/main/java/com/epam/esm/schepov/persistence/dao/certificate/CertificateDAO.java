package com.epam.esm.schepov.persistence.dao.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.dao.certificate.impl.JdbcCertificateDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.persistence.sort.CertificateSortParameter;

import java.util.Set;

/**
 * An interface which describes CRUD methods that its implementations provide
 * to work with {@code GiftCertificates} in a storage.
 *
 * @author Igor Schepov
 * @see GiftCertificate
 * @see JdbcCertificateDAO
 * @see CertificateSortParameter
 * @since 1.0
 */
public interface CertificateDAO {

    /**
     * Description of a method which returns
     * all the certificates stored.
     *
     * @return All certificates from the storage.
     */
    Set<GiftCertificate> getAll();

    /**
     * Description of a method which returns
     * all the certificates stored in order described by {@code sortParameter}
     * and {@code inDescendingOrder}.
     * <p>
     * The parameter {@code sortParameter} represents a property of certificates, by which
     * the returned certificates are ordered.
     * <p>
     * If the parameter {@code inDescendingOrder} is true, the order of the returned certificates
     * is descending.
     *
     * @param sortParameter     A property of certificates, by which
     *                          the returned certificates are ordered.
     * @param inDescendingOrder If true the order of the returned certificates
     *                          is descending.
     * @return All certificates from the storage ordered according to the parameters passed.
     */
    Set<GiftCertificate> getAll(CertificateSortParameter sortParameter, boolean inDescendingOrder);

    /**
     * Description of a method which returns the certificate
     * with identifier equal to {@code id}.
     *
     * @param id The identifier of the requested certificate.
     * @return The certificate with the specified identifier.
     */
    GiftCertificate getById(int id);

    /**
     * Description of a method which returns the certificate
     * with name equal to {@code name}.
     *
     * @param name The name of the requested certificate.
     * @return The certificate with the specified name.
     */
    GiftCertificate getByName(String name);

    /**
     * Description of a method which inserts the passed {@code giftCertificate}.
     *
     * @param giftCertificate The certificate to insert.
     * @throws DaoException Thrown in case of invalid {@code giftCertificate}'s properties.
     */
    void insert(GiftCertificate giftCertificate) throws DaoException;

    /**
     * Description of a method which deletes the certificate with the specified {@code id}.
     *
     * @param id The identifier of the certificate to be deleted.
     */
    void delete(int id);

    /**
     * Description of a method which updates the certificate with the specified {@code id}
     * using the {@code giftCertificate} parameter.
     *
     * @param id              The identifier of the certificate to be updated.
     * @param giftCertificate The updated value of the certificate.
     * @throws DaoException Thrown in case of invalid {@code giftCertificate}'s properties.
     */
    void update(int id, GiftCertificate giftCertificate) throws DaoException;
}
