package com.epam.esm.schepov.persistence.dao.certificatetag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static com.epam.esm.schepov.persistence.dao.Column.CERTIFICATE_ID;
import static com.epam.esm.schepov.persistence.dao.Column.ID;

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


    @Autowired
    public JdbcCertificateTagDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    @Override
    public CertificateTag getById(int id) {
        return Objects.requireNonNull(jdbcOperations.query(FIND_BY_ID_QUERY, this::mapResultSet, id))
                .stream().findAny().orElse(null);
    }

    @Override
    public void insert(CertificateTag certificateTag) {
        jdbcOperations.update(INSERT_QUERY, certificateTag.getCertificateId(), certificateTag.getTagId());
    }

    @Override
    public CertificateTag getByCertificateIdAndTagId(int certificateId, int tagId) {
        return Objects.requireNonNull(jdbcOperations.
                query(FIND_BY_CERTIFICATE_AND_TAG_ID_QUERY, this::mapResultSet, certificateId, tagId))
                .stream().findAny().orElse(null);
    }

    @Override
    public void deleteByCertificateId(int id) throws DaoException {
        try {
            jdbcOperations.update(DELETE_BY_CERTIFICATE_ID_QUERY, id);
        } catch (DataAccessException exception) {
            throw new DaoException("Can't delete CertificateTag with certificate id = " + id, exception);
        }
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
