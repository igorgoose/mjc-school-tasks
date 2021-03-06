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

    @Autowired
    public JdbcCertificateDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    @Override
    public Set<GiftCertificate> getAll() {
        return jdbcOperations.query(GET_ALL_CERTIFICATES_QUERY, this::mapResultSet);
    }

    @Override
    public Set<GiftCertificate> getAll(CertificateSortParameter sortParameter, boolean inDescendingOrder) {
        return jdbcOperations.query(GET_ALL_CERTIFICATES_QUERY + sortParameter.getSortParameter(inDescendingOrder),
                this::mapResultSet);
    }

    @Override
    public GiftCertificate getById(int id) {
        return Objects.requireNonNull(jdbcOperations.query(GET_CERTIFICATE_BY_ID_QUERY, this::mapResultSet, id))
                .stream().findAny().orElse(null);
    }

    @Override
    public GiftCertificate getByName(String name) {
        return Objects.requireNonNull(jdbcOperations.query(GET_CERTIFICATE_BY_NAME_QUERY, this::mapResultSet, name))
                .stream().findAny().orElse(null);
    }

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

    @Override
    public void delete(int id) throws DaoException {
        try {
            jdbcOperations.update(DELETE_CERTIFICATE_QUERY, id);
        } catch (DataAccessException exception) {
            throw new DaoException("Can't delete certificate with id = " + id, exception);
        }
    }

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
