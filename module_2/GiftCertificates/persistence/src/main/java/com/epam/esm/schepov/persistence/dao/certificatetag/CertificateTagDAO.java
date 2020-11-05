package com.epam.esm.schepov.persistence.dao.certificatetag;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.persistence.dao.certificatetag.impl.JdbcCertificateTagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;

/**
 * An interface which describes CRD methods that its implementations provide
 * to work with {@code CertificateTags} in a storage.
 *
 * @author Igor Schepov
 * @see CertificateTag
 * @see JdbcCertificateTagDAO
 * @since 1.0
 */
public interface CertificateTagDAO {

    /**
     * Description of a method which returns the {@code CertificateTag}
     * with identifier equal to {@code id}.
     *
     * @param id The identifier of the requested {@code CertificateTag}.
     * @return The {@code CertificateTag} with the specified identifier.
     */
    CertificateTag getById(int id);

    /**
     * Description of a method which inserts the passed {@code CertificateTag}.
     *
     * @param certificateTag The certificate to insert.
     *
     */
    void insert(CertificateTag certificateTag);

    /**
     * Description of a method which returns the {@code CertificateTag}
     * with specified certificate's {@code id} and tag's {@code id}.
     *
     * @param certificateId The identifier of the requested {@code CertificateTag}'s
     *                      certificate.
     * @param tagId The identifier of the requested {@code CertificateTag}'s tag.
     * @return The {@code CertificateTag} with the specified identifiers.
     */
    CertificateTag getByCertificateIdAndTagId(int certificateId, int tagId);

    /**
     * Description of a method which deletes the {@code CertificateTag}
     * with the specified {@code id}.
     *
     * @param id The identifier of the certificate to be deleted.
     */
    void deleteByCertificateId(int id);
}
