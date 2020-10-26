package com.epam.esm.schepov.persistence.dao.certificatetag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        return jdbcOperations.queryForObject(FIND_BY_ID_QUERY, this::mapRow, id);
    }

    @Override
    public void insert(CertificateTag certificateTag) {
        jdbcOperations.update(INSERT_QUERY, certificateTag.getCertificateId(), certificateTag.getTagId());
    }

    @Override
    public CertificateTag getByCertificateIdAndTagId(int certificateId, int tagId) {
        return jdbcOperations.queryForObject(FIND_BY_CERTIFICATE_AND_TAG_ID_QUERY, this::mapRow, certificateId, tagId);
    }

    @Override
    public void deleteByCertificateId(int id) {
        jdbcOperations.update(DELETE_BY_CERTIFICATE_ID_QUERY, id);
    }

    private CertificateTag mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        if(resultSet.next()){
            return new CertificateTag(
                    resultSet.getInt(ID.getName()),
                    resultSet.getInt(CERTIFICATE_ID.getName()),
                    resultSet.getInt(CERTIFICATE_ID.getName()));
        }
        throw new RuntimeException("not found");
    }
}
