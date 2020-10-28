package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GiftCertificateServiceImplTest {

    private static GiftCertificateService giftCertificateService;
    private static final GiftCertificate giftCertificate1 = new GiftCertificate(1, "1", "1", 1,
            new Date(), new Date(), 1);
    private static final GiftCertificate giftCertificate2 = new GiftCertificate(2, "2", "2", 2,
            new Date(), new Date(), 2);
    private static Set<GiftCertificate> giftCertificateSet;


    @BeforeAll
    static void setUp() {
        CertificateDAO certificateDAO = Mockito.mock(CertificateDAO.class);
        giftCertificateSet = new LinkedHashSet<>();
        giftCertificateSet.add(giftCertificate1);
        giftCertificateSet.add(giftCertificate2);

        Mockito.when(certificateDAO.getAll()).thenReturn(giftCertificateSet);

        Mockito.when(certificateDAO.getById(1)).thenReturn(giftCertificate1);
        Mockito.when(certificateDAO.getById(2)).thenReturn(giftCertificate2);
        Mockito.when(certificateDAO.getById(Mockito.intThat(arg -> arg != 1 && arg != 2))).
                thenReturn(null);

        Mockito.when(certificateDAO.getByName("1")).thenReturn(giftCertificate1);

        Mockito.when(certificateDAO.getByName("2")).thenReturn(giftCertificate1);
        Mockito.when(certificateDAO.getByName(Mockito.argThat(arg -> !"2".equals(arg) && !"1".equals(arg))))
                .thenReturn(null);

        Mockito.doNothing().when(certificateDAO).insert(Mockito.argThat(arg ->
                !"1".equals(arg.getName()) && !"2".equals(arg.getName())));
        Mockito.doThrow(new DaoException()).when(certificateDAO).insert(Mockito.argThat(arg ->
                "1".equals(arg.getName()) || "2".equals(arg.getName())));

        Mockito.doNothing().when(certificateDAO).update(Mockito.intThat(arg -> arg == 1),
                Mockito.argThat(arg -> !"1".equals(arg.getName()) && !"2".equals(arg.getName())));
        Mockito.doThrow(new DaoException()).when(certificateDAO).
                update(Mockito.intThat(arg -> arg != 1 && arg != 2), Mockito.any());
        Mockito.doThrow(new DaoException()).when(certificateDAO).
                update(Mockito.intThat(arg -> arg != 1),
                        Mockito.argThat(arg -> "1".equals(arg.getName())));
        Mockito.doThrow(new DaoException()).when(certificateDAO).
                update(Mockito.intThat(arg -> arg != 2),
                        Mockito.argThat(arg -> "2".equals(arg.getName())));

        Mockito.doNothing().when(certificateDAO).delete(Mockito.intThat(arg -> arg == 1 || arg == 2));
        Mockito.doThrow(new DaoException()).when(certificateDAO).delete(Mockito.intThat(arg -> arg != 1 && arg != 2));

        giftCertificateService = new GiftCertificateServiceImpl(certificateDAO);
    }

    @Test
    void getAllCertificatesPositive() {
        assertEquals(giftCertificateSet, giftCertificateService.getAllCertificates());
    }

    @Test
    void getCertificateByIdPositive() {
        assertEquals(giftCertificate1, giftCertificateService.getCertificateById(1));
    }

    @Test
    void getCertificateByInvalidId() {
        assertThrows(ResourceNotFoundServiceException.class, () -> giftCertificateService.getCertificateById(3));
    }

    @Test
    void insertCertificatePositive() {
        giftCertificateService.insertCertificate(
                new GiftCertificate(0, "3", "3", 3, new Date(), new Date(), 3));
    }

    @Test
    void insertCertificateDuplicateName() {
        assertThrows(ResourceConflictServiceException.class, () -> giftCertificateService.insertCertificate(
                new GiftCertificate(0, "2", "3", 3, new Date(), new Date(), 3)));
    }

    @Test
    void deleteCertificatePositive() {
        giftCertificateService.deleteCertificate(1);
    }

    @Test
    void deleteCertificateInvalidId() {
        assertThrows(ResourceNotFoundServiceException.class, () -> giftCertificateService.deleteCertificate(3));
    }

    @Test
    void updateCertificate() {
        assertEquals(giftCertificate1, giftCertificateService.updateCertificate(1, giftCertificate1));
    }

    @Test
    void updateCertificateInvalidId() {
        assertThrows(ResourceNotFoundServiceException.class, () ->
                giftCertificateService.updateCertificate(3,
                        new GiftCertificate(0, "3", "3", 3, new Date(), new Date(), 3)));
    }

    @Test
    void updateCertificateDuplicateName() {
        assertThrows(ResourceConflictServiceException.class, () ->
                giftCertificateService.updateCertificate(2,
                        new GiftCertificate(2, "1", "3", 3, new Date(), new Date(), 3)));
    }

}