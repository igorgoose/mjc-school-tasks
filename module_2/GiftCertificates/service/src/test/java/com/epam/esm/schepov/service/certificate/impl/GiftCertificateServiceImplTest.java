package com.epam.esm.schepov.service.certificate.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
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
    private static final GiftCertificate giftCertificate1 =
            new GiftCertificate(1, "1", "1", 1, new Date(), new Date(), 1);
    private static final GiftCertificate giftCertificate2 =
            new GiftCertificate(2, "2", "2", 2, new Date(), new Date(), 2);
    private static final GiftCertificate giftCertificateToInsert =
            new GiftCertificate(3, "3", "3", 3, new Date(), new Date(), 3);
    private static Set<GiftCertificate> giftCertificateSet;

    private static final Tag tag1 = new Tag(1, "name1");
    private static final Tag tag2 = new Tag(2, "name2");

    private static final CertificateTag certificateTag1 = new CertificateTag(1, 1, 1);


    @BeforeAll
    static void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(setUpCertificateDAO(),
                setUpTagDAO(), setUpCertificateTagDAO());
    }

    private static CertificateTagDAO setUpCertificateTagDAO() {
        CertificateTagDAO certificateTagDAO = Mockito.mock(CertificateTagDAO.class);

        Mockito.when(certificateTagDAO.getById(1)).thenReturn(certificateTag1);
        Mockito.when(certificateTagDAO.getById(Mockito.intThat(arg -> arg != 1)))
                .thenReturn(null);

        Mockito.when(certificateTagDAO.getByCertificateIdAndTagId(1, 1)).
                thenReturn(certificateTag1);
        Mockito.when(certificateTagDAO.getByCertificateIdAndTagId(Mockito.intThat(arg -> arg != 1),
                Mockito.intThat(arg -> arg != 1))).thenReturn(null);

        Mockito.doNothing().when(certificateTagDAO).deleteByCertificateId(Mockito.anyInt());

        Mockito.doNothing().when(certificateTagDAO).insert(Mockito.any());

        return certificateTagDAO;
    }


    private static CertificateDAO setUpCertificateDAO() {
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
        Mockito.when(certificateDAO.getByName("2")).thenReturn(giftCertificate2);
        Mockito.when(certificateDAO.getByName("3")).thenReturn(giftCertificateToInsert);
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

        return certificateDAO;
    }

    private static TagDAO setUpTagDAO() {
        TagDAO tagDAO = Mockito.mock(TagDAO.class);
        Set<Tag> allTags = new LinkedHashSet<>();
        allTags.add(tag1);
        allTags.add(tag2);
        Mockito.when(tagDAO.getAll()).thenReturn(allTags);

        Mockito.when(tagDAO.getById(1)).thenReturn(tag1);
        Mockito.when(tagDAO.getById(2)).thenReturn(tag2);
        Mockito.when(tagDAO.getById(Mockito.intThat(arg -> arg != 1 && arg != 2))).thenReturn(null);

        Mockito.when(tagDAO.getByName("name1")).thenReturn(tag1);
        Mockito.when(tagDAO.getByName("name2")).thenReturn(tag2);
        Mockito.when(tagDAO.getByName(Mockito.argThat(arg -> !(arg.equals("name1") || arg.equals("name2")))))
                .thenReturn(null);

        Mockito.doNothing().when(tagDAO).delete(Mockito.intThat(arg -> arg == 1 || arg == 2));
        Mockito.doThrow(new DaoException()).when(tagDAO).delete(Mockito.intThat(arg -> !(arg == 1 || arg == 2)));

        Mockito.doNothing().when(tagDAO).insert(Mockito.argThat(arg ->
                !("name1".equals(arg.getName()) || "name2".equals(arg.getName()))));

        Mockito.doThrow(new DaoException()).when(tagDAO).insert(Mockito.argThat(arg ->
                ("name1".equals(arg.getName()) || "name2".equals(arg.getName()))));

        return tagDAO;
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
        assertThrows(InvalidRequestDataServiceException.class, () -> giftCertificateService.deleteCertificate(3));
    }

    @Test
    void updateCertificate() {
        assertEquals(giftCertificate1, giftCertificateService.updateCertificate(1, giftCertificate1));
    }

    @Test
    void updateCertificateInvalidId() {
        assertThrows(InvalidRequestDataServiceException.class, () ->
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