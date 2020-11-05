package com.epam.esm.schepov.persistence.dao.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.Column;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.persistence.sort.CertificateSortParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static com.epam.esm.schepov.persistence.dao.Column.*;

/**
 * {@code JdbcCertificateDAO} provides methods to interact with a database
 * and perform CRD operations on certificates. The class utilizes a {@link JdbcOperations}
 * object, hence the name.
 *
 * @author Igor Schepov
 * @see GiftCertificate
 * @see CertificateDAO
 * @see CertificateSortParameter
 * @since 1.0
 */
@Repository
public class JdbcCertificateDAO implements CertificateDAO {

    private static final String JOIN_TAGS =
            "left join certificate_tags ct on cs.id = ct.certificate_id " +
                    "left join tags on ct.tag_id = tags.id ";

    private static final String GET_ALL_CERTIFICATES_QUERY =
            "select cs.id id, cs.name name, description, price, create_date, last_update_date, duration, " +
                    "tags.id t_id, tags.name t_name " +
                    "from certificates cs " +
                    JOIN_TAGS;

    private static final String GET_CERTIFICATE_BY_ID_QUERY =
            "select cs.id id, cs.name name, description, price, create_date, last_update_date, duration, " +
                    "tags.id t_id, tags.name t_name " +
                    "from certificates cs " +
                    JOIN_TAGS + "" +
                    "where cs.id = ?";

    private static final String GET_CERTIFICATE_BY_NAME_QUERY =
            "select cs.id id, cs.name name, description, price, create_date, last_update_date, duration, " +
                    "tags.id t_id, tags.name t_name " +
                    "from certificates cs " +
                    JOIN_TAGS + "" +
                    "where cs.name = ?";

    private static final String INSERT_CERTIFICATE_QUERY =
            "insert into certificates(name, description, price, create_date, last_update_date, duration) " +
                    "values(?, ?, ?, ?, ?, ?)";

    private static final String DELETE_CERTIFICATE_QUERY =
            "delete from certificates where id = ?";

    private static final String UPDATE_CERTIFICATE_QUERY =
            "update certificates " +
                    "set name = ?, description = ?, price = ?, last_update_date = ?, duration = ? " +
                    "where id = ?";

    private final JdbcOperations jdbcOperations;

    /**
     * Injects a {@code JdbcOperations} object which is used to interact with the database.
     *
     * @param jdbcOperations A JdbcOperations object to interact with the database.
     */
    @Autowired
    public JdbcCertificateDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    /**
     * Returns all the certificates stored.
     *
     * @return All certificates from the storage.
     */
    @Override
    public Set<GiftCertificate> getAll() {
        return jdbcOperations.query(GET_ALL_CERTIFICATES_QUERY, this::mapResultSet);
    }

    /**
     * Returns all the certificate stored in order described by {@code sortParameter}
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
    @Override
    public Set<GiftCertificate> getAll(CertificateSortParameter sortParameter, boolean inDescendingOrder) {
        return jdbcOperations.query(GET_ALL_CERTIFICATES_QUERY + sortParameter.getSortParameter(inDescendingOrder),
                this::mapResultSet);
    }

    /**
     * Returns the certificate with identifier equal to {@code id} passed as a parameter.
     *
     * @param id The identifier of the requested certificate.
     * @return The certificate with the specified identifier.
     */
    @Override
    public GiftCertificate getById(int id) {
        return Objects.requireNonNull(jdbcOperations.query(GET_CERTIFICATE_BY_ID_QUERY, this::mapResultSet, id))
                .stream().findAny().orElse(null);
    }

    /**
     * Returns the certificate with name equal to {@code name} passed as a parameter.
     *
     * @param name The name of the requested certificate.
     * @return The certificate with the specified name.
     */
    @Override
    public GiftCertificate getByName(String name) {
        return Objects.requireNonNull(jdbcOperations.query(GET_CERTIFICATE_BY_NAME_QUERY, this::mapResultSet, name))
                .stream().findAny().orElse(null);
    }

    /**
     * Inserts the passed {@code certificate}.
     *
     * @param giftCertificate The certificate to insert.
     * @throws DaoException Thrown in case of invalid {@code certificate}'s properties.
     */
    @Override
    public void insert(GiftCertificate giftCertificate) throws DaoException {
        try {
            jdbcOperations.update(INSERT_CERTIFICATE_QUERY,
                    giftCertificate.getName(),
                    giftCertificate.getDescription(),
                    giftCertificate.getPrice(),
                    giftCertificate.getCreateDate(),
                    giftCertificate.getLastUpdateDate(),
                    giftCertificate.getDuration());
        } catch (DataAccessException exception) {
            throw new DaoException("Certificate with name " + giftCertificate.getName() + " already exists", exception);
        }
    }

    /**
     * Deletes the certificate with the specified {@code id};
     *
     * @param id The identifier of the certificate to be deleted.
     */
    @Override
    public void delete(int id) throws DaoException {
        jdbcOperations.update(DELETE_CERTIFICATE_QUERY, id);
    }

    /**
     * Updates the certificate with the specified {@code id}
     * using the {@code giftCertificate} parameter.
     *
     * @param id              The identifier of the certificate to be updated.
     * @param giftCertificate The updated value of the certificate.
     * @throws DaoException Thrown in case of invalid {@code giftCertificate}'s properties.
     */
    @Override
    public void update(int id, GiftCertificate giftCertificate) throws DaoException {
        try {
            jdbcOperations.update(UPDATE_CERTIFICATE_QUERY,
                    giftCertificate.getName(),
                    giftCertificate.getDescription(),
                    giftCertificate.getPrice(),
                    new Date(),
                    giftCertificate.getDuration(),
                    id);
        } catch (DataAccessException exception) {
            throw new DaoException("Can't delete certificate with id = " + id, exception);
        }
    }

    private Set<GiftCertificate> mapResultSet(ResultSet resultSet) throws SQLException {
        Set<GiftCertificate> giftCertificates = new LinkedHashSet<>();
        while (resultSet.next()) {
            GiftCertificate giftCertificate = mapGiftCertificate(resultSet);
            for (GiftCertificate certificateFromSet : giftCertificates) {
                if (giftCertificate.equals(certificateFromSet)) {
                    giftCertificate.getTags().addAll(certificateFromSet.getTags());
                }
            }
            giftCertificates.remove(giftCertificate);
            giftCertificates.add(giftCertificate);
        }
        return giftCertificates;
    }


    private GiftCertificate mapGiftCertificate(ResultSet resultSet) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate(
                resultSet.getInt(ID.getName()),
                resultSet.getString(NAME.getName()),
                resultSet.getString(DESCRIPTION.getName()),
                resultSet.getFloat(PRICE.getName()),
                resultSet.getTimestamp(CREATE_DATE.getName()),
                resultSet.getTimestamp(LAST_UPDATE_DATE.getName()),
                resultSet.getInt(DURATION.getName())
        );
        int tagId = resultSet.getInt(Column.TAGS_ID.getName());
        String tagName = resultSet.getString(TAG_NAME.getName());
        if (tagName != null) {
            giftCertificate.getTags().add(new Tag(tagId, tagName));
        }
        return giftCertificate;
    }
}
