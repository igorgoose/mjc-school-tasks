package com.epam.esm.schepov.persistence.dao.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Date;

class JdbcCertificateDAOTest {

    private static EmbeddedDatabase database;
    private static CertificateDAO certificateDAO;

    @BeforeAll
    public static void setUp() {
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("create_tables.sql", "initialize_data.sql")
                .build();
        certificateDAO = new JdbcCertificateDAO(new JdbcTemplate(database));
    }

    @Test
    public void getByIdPositive() {
        GiftCertificate certificate = certificateDAO.getById(1);
        assertTrue(certificate.getId() == 1 &&
                certificate.getName().equals("parachute jump") &&
                certificate.getPrice() == 50.0 &&
                certificate.getDescription() == null &&
                certificate.getDuration() == 7);
    }

    @Test
    public void getByIdNotFound() {
        assertNull(certificateDAO.getById(99));
    }

    @Test
    public void getByNamePositive() {
        GiftCertificate certificate = certificateDAO.getByName("parachute jump");
        assertTrue(certificate.getId() == 1 &&
                certificate.getName().equals("parachute jump") &&
                certificate.getPrice() == 50.0 &&
                certificate.getDescription() == null &&
                certificate.getDuration() == 7);
    }

    @Test
    public void getByNameNotFound() {
        assertNull(certificateDAO.getByName("non-existent name"));
    }


    @Test
    public void insertPositive() {
        GiftCertificate newCertificate = new GiftCertificate(0, "name", "description", 1,
                null, null, 1);
        certificateDAO.insert(newCertificate);
        GiftCertificate persistedCertificate = certificateDAO.getByName("name");
        newCertificate.setId(persistedCertificate.getId());
        newCertificate.setCreateDate(persistedCertificate.getCreateDate());
        newCertificate.setLastUpdateDate(persistedCertificate.getLastUpdateDate());
        assertEquals(newCertificate, persistedCertificate);
    }

    @Test
    public void insertDuplicateName() {
        assertThrows(DaoException.class, () -> certificateDAO.insert(new GiftCertificate(
                0, "yoga class", "", 1, null, null, 1)));
    }

    @Test
    public void deletePositive() {
        certificateDAO.delete(3);
        assertNull(certificateDAO.getById(3));
    }

    @Test
    public void updatePositive() {
        GiftCertificate updatedCertificate = new GiftCertificate(2, "updated", "updated",
                1, null, null, 1);
        certificateDAO.update(2, updatedCertificate);
        GiftCertificate persistedCertificate = certificateDAO.getById(2);
        updatedCertificate.setCreateDate(persistedCertificate.getCreateDate());
        updatedCertificate.setLastUpdateDate(persistedCertificate.getLastUpdateDate());
        assertEquals(updatedCertificate, persistedCertificate);
    }

}