package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.CertificateServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

class GiftCertificateServiceImplTest {

    private static GiftCertificateService giftCertificateService;
    private static final GiftCertificate giftCertificate1 = new GiftCertificate(1, "1", "1", 1,
            new Date(), new Date(), 1);
    private static final GiftCertificate giftCertificate2 = new GiftCertificate(2, "2", "2", 2,
            new Date(), new Date(), 2);
    private static Set<GiftCertificate> giftCertificateSet;


    @BeforeAll
    static void setUp(){
        CertificateDAO certificateDAO = Mockito.mock(CertificateDAO.class);
        giftCertificateSet = new LinkedHashSet<>();
        giftCertificateSet.add(giftCertificate1);
        giftCertificateSet.add(giftCertificate2);

        Mockito.when(certificateDAO.getAll()).thenReturn(giftCertificateSet);

        Mockito.when(certificateDAO.getById(1)).thenReturn(giftCertificate1);
        Mockito.when(certificateDAO.getById(Mockito.intThat(arg -> arg != 1 && arg != 2))).
                thenReturn(null);

        Mockito.when(certificateDAO.getByName("2")).thenReturn(giftCertificate1);
        Mockito.when(certificateDAO.getByName(Mockito.argThat(arg -> !"2".equals(arg) && !"1".equals(arg))))
                .thenReturn(null);

        Mockito.doNothing().when(certificateDAO).insert(Mockito.argThat(arg ->
                !"1".equals(arg.getName()) && !"2".equals(arg.getName())));
        Mockito.doThrow(new DaoException()).when(certificateDAO).insert(Mockito.argThat(arg ->
                "1".equals(arg.getName()) || "2".equals(arg.getName())));

        Mockito.doNothing().when(certificateDAO).update(Mockito.intThat(arg -> arg == 1 || arg == 2), Mockito.any());
        Mockito.doThrow(new DaoException()).when(certificateDAO).
                update(Mockito.intThat(arg -> arg != 1 && arg != 2), Mockito.any());

        Mockito.doNothing().when(certificateDAO).delete(Mockito.intThat(arg -> arg == 1 || arg == 2));
        Mockito.doThrow(new DaoException()).when(certificateDAO).delete(Mockito.intThat(arg -> arg != 1 && arg != 2));

        giftCertificateService = new GiftCertificateServiceImpl(certificateDAO);
    }

    @Test
    void getAllCertificatesPositive() {
        assertEquals(giftCertificateSet, giftCertificateService.getAllCertificates());
    }

    @Test
    void getCertificateByIdPositive() throws CertificateServiceException {
        assertEquals(giftCertificate1, giftCertificateService.getCertificateById(1));
    }

    @Test
    void getCertificateByInvalidId() {
        assertThrows(CertificateServiceException.class, () -> giftCertificateService.getCertificateById(3));
    }

    @Test
    void insertCertificatePositive() throws CertificateServiceException {
        giftCertificateService.insertCertificate(
                new GiftCertificate(0, "3", "3", 3, new Date(), new Date(), 3));
    }

    @Test
    void insertCertificateDuplicateName() {
        assertThrows(CertificateServiceException.class, () -> giftCertificateService.insertCertificate(
                new GiftCertificate(0, "2", "3", 3, new Date(), new Date(), 3)));
    }

    @Test
    void deleteCertificatePositive() throws CertificateServiceException {
        giftCertificateService.deleteCertificate(1);
    }

    @Test
    void deleteCertificateInvalidId() {
        assertThrows(CertificateServiceException.class, () -> giftCertificateService.deleteCertificate(3));
    }

    @Test
    void updateCertificate() throws CertificateServiceException {
        giftCertificateService.updateCertificate(1, giftCertificate1);
    }

    @Test
    void updateCertificateInvalidId() {
        assertThrows(CertificateServiceException.class, () ->
                giftCertificateService.updateCertificate(3, giftCertificate1));
    }

}