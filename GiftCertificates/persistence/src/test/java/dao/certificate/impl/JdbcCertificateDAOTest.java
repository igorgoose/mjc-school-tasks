package dao.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import dao.certificate.CertificateDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class JdbcCertificateDAOTest {

    private static EmbeddedDatabase database;
    private static CertificateDAO certificateDAO;

    @BeforeAll
    public static void setUp(){
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("create_tables.sql", "initialize_data.sql")
                .build();
        certificateDAO = new JdbcCertificateDAO(new JdbcTemplate(database));
    }

    @Test
    public void getCertificateByIdTest(){
        GiftCertificate certificate = certificateDAO.getCertificateById(1);
        assertTrue(certificate.getId() == 1 &&
                certificate.getName().equals("parachute jump") &&
                certificate.getPrice() == 50.0 &&
                certificate.getDescription() == null &&
                certificate.getDuration() == 7);
    }

    @Test
    public void getCertificateByNameTest(){
        GiftCertificate certificate = certificateDAO.getCertificateByName("parachute jump");
        assertTrue(certificate.getId() == 1 &&
                certificate.getName().equals("parachute jump") &&
                certificate.getPrice() == 50.0 &&
                certificate.getDescription() == null &&
                certificate.getDuration() == 7);
    }
}