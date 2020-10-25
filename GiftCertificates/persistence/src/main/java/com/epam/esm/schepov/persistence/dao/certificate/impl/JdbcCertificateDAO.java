package com.epam.esm.schepov.persistence.dao.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
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

    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcCertificateDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    @Override
    public Set<GiftCertificate> getAllCertificates() {
        return jdbcOperations.query(GET_ALL_CERTIFICATES_QUERY, this::mapResultSet);
    }

    @Override
    public GiftCertificate getCertificateById(int id) {
        //todo create custom exceptions
        return jdbcOperations.query(GET_CERTIFICATE_BY_ID_QUERY, this::mapResultSet, id)
                .stream().findAny().orElseThrow(() -> new RuntimeException("not found"));
    }

    @Override
    public GiftCertificate getCertificateByName(String name) {
        return jdbcOperations.query(GET_CERTIFICATE_BY_NAME_QUERY, this::mapResultSet, name)
                .stream().findAny().orElseThrow(() -> new RuntimeException("not found"));
    }

    @Override
    public void insertCertificate(GiftCertificate giftCertificate) {
        jdbcOperations.update(INSERT_CERTIFICATE_QUERY,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate(),
                giftCertificate.getDuration());
    }

    @Override
    public void deleteCertificate(int id) {
        jdbcOperations.update(DELETE_CERTIFICATE_QUERY);
    }

    private Set<GiftCertificate> mapResultSet(ResultSet resultSet) throws SQLException {
        Set<GiftCertificate> giftCertificates = new LinkedHashSet<>();
        while(resultSet.next()){
            GiftCertificate giftCertificate = mapGiftCertificate(resultSet);
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
        int tagId = resultSet.getInt(TAG_ID.getName());
        String tagName = resultSet.getString(TAG_NAME.getName());
        if(tagName != null) {
            giftCertificate.getTags().add(new Tag(tagId, tagName));
        }
        return giftCertificate;
    }
}
