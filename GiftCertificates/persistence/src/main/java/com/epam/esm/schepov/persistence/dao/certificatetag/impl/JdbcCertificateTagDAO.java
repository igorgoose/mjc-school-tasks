package com.epam.esm.schepov.persistence.dao.certificatetag.impl;

import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCertificateTagDAO implements CertificateTagDAO {

    private static final String INSERT_QUERY = "insert into certificate_tags(certificate_id, tag_id)" +
            " values(?, ?)";

    private final JdbcOperations jdbcOperations;


    @Autowired
    public JdbcCertificateTagDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    @Override
    public void insertCertificateTag(int certificateId, int tagId) {
        jdbcOperations.update(INSERT_QUERY, certificateId, tagId);
    }
}
