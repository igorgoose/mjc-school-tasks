package com.epam.esm.schepov.persistence.dao.certificatetag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static com.epam.esm.schepov.persistence.dao.Column.CERTIFICATE_ID;
import static com.epam.esm.schepov.persistence.dao.Column.ID;

/**
 * {@code JdbcCertificateTagDAO} provides methods to interact with a database
 * and perform CRD operations on {@code CertificateTag}s. The class utilizes a {@link JdbcOperations}
 * object, hence the name.
 *
 * @author Igor Schepov
 * @see CertificateTag
 * @see CertificateTagDAO
 * @since 1.0
 */
@Repository
public class JdbcCertificateTagDAO implements CertificateTagDAO {

    private static final String FIND_BY_ID_QUERY = "select id, certificate_id, tag_id " +
            "from certificate_tags " +
            "where id = ?";

    private static final String FIND_BY_CERTIFICATE_AND_TAG_ID_QUERY = "select id, certificate_id, tag_id " +
            "from certificate_tags " +
            "where certificate_id = ? and tag_id = ?";

    private static final String INSERT_QUERY = "insert into certificate_tags(certificate_id, tag_id)" +
            " values(?, ?)";

    private static final String DELETE_BY_CERTIFICATE_ID_QUERY = "delete from certificate_tags " +
            "where certificate_id = ?";

    private final JdbcOperations jdbcOperations;


    /**
     * Injects a {@code JdbcOperations} object which is used to interact with the database.
     *
     * @param jdbcOperations A JdbcOperations object to interact with the database.
     */
    @Autowired
    public JdbcCertificateTagDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    /**
     * Returns the {@code CertificateTag}
     * with identifier equal to {@code id}.
     *
     * @param id The identifier of the requested {@code CertificateTag}.
     * @return The {@code CertificateTag} with the specified identifier.
     */
    @Override
    public CertificateTag getById(int id) {
        return Objects.requireNonNull(jdbcOperations.query(FIND_BY_ID_QUERY, this::mapResultSet, id))
                .stream().findAny().orElse(null);
    }

    /**
     * Inserts the passed {@code CertificateTag}.
     *
     * @param certificateTag The certificate to insert.
     */
    @Override
    public void insert(CertificateTag certificateTag) {
        jdbcOperations.update(INSERT_QUERY, certificateTag.getCertificateId(), certificateTag.getTagId());
    }

    /**
     * Returns the {@code CertificateTag}
     * with specified certificate's {@code id} and tag's {@code id}.
     *
     * @param certificateId The identifier of the requested {@code CertificateTag}'s
     *                      certificate.
     * @param tagId         The identifier of the requested {@code CertificateTag}'s tag.
     * @return The {@code CertificateTag} with the specified identifiers.
     */
    @Override
    public CertificateTag getByCertificateIdAndTagId(int certificateId, int tagId) {
        return Objects.requireNonNull(jdbcOperations.
                query(FIND_BY_CERTIFICATE_AND_TAG_ID_QUERY, this::mapResultSet, certificateId, tagId))
                .stream().findAny().orElse(null);
    }

    /**
     * Deletes the {@code CertificateTag}
     * with the specified {@code id}.
     *
     * @param id The identifier of the certificate to be deleted.
     */
    @Override
    public void deleteByCertificateId(int id) {
        jdbcOperations.update(DELETE_BY_CERTIFICATE_ID_QUERY, id);
    }

    private Set<CertificateTag> mapResultSet(ResultSet resultSet) throws SQLException {
        Set<CertificateTag> certificateTagSet = new LinkedHashSet<>();
        while (resultSet.next()) {
            certificateTagSet.add(mapRow(resultSet));
        }
        return certificateTagSet;
    }

    private CertificateTag mapRow(ResultSet resultSet) throws SQLException {
        return new CertificateTag(
                resultSet.getInt(ID.getName()),
                resultSet.getInt(CERTIFICATE_ID.getName()),
                resultSet.getInt(CERTIFICATE_ID.getName()));

    }
}
