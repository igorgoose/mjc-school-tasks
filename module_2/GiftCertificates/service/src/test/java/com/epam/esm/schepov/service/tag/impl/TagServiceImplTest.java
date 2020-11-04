package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

class TagServiceImplTest {


    private static TagService tagService;
    private static final Tag tag1 = new Tag(1, "name1");
    private static final Tag tag2 = new Tag(2, "name2");
    private static Set<Tag> allTags;

    private static final CertificateTag certificateTag1 = new CertificateTag(1, 1, 1);

    private static final GiftCertificate giftCertificate1 =
            new GiftCertificate(1, "1", "1", 1, new Date(), new Date(), 1);
    private static final GiftCertificate giftCertificate2 =
            new GiftCertificate(2, "2", "2", 2, new Date(), new Date(), 2);
    private static final GiftCertificate giftCertificateToInsert =
            new GiftCertificate(3, "3", "3", 3, new Date(), new Date(), 3);

    @BeforeAll
    static void setUp() {
        tagService = new TagServiceImpl(setUpTagDAO(), setUpCertificateDAO(), setUpCertificateTagDAO());

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
        Set<GiftCertificate> giftCertificateSet = new LinkedHashSet<>();
        giftCertificateSet.add(giftCertificate1);
        giftCertificateSet.add(giftCertificate2);

        Mockito.when(certificateDAO.getAll(Mockito.any(), Mockito.anyBoolean())).thenReturn(giftCertificateSet);

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
        allTags = new LinkedHashSet<>();
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

        Mockito.doNothing().when(tagDAO).insert(Mockito.argThat(arg -> {
                    boolean nameIsValid = !("name1".equals(arg.getName()) || "name2".equals(arg.getName()));
                    boolean allCertificatesAreValid = true;
                    for (GiftCertificate certificate : arg.getGiftCertificates()) {
                        allCertificatesAreValid = allCertificatesAreValid && certificate.getId() > 0
                                && certificate.getId() < 3;
                    }
                    return nameIsValid && allCertificatesAreValid;
                }
        ));

        Mockito.doThrow(new DaoException()).when(tagDAO).insert(Mockito.argThat(arg ->
                {
                    boolean nameIsValid = !("name1".equals(arg.getName()) || "name2".equals(arg.getName()));
                    boolean allCertificatesAreValid = true;
                    for (GiftCertificate certificate : arg.getGiftCertificates()) {
                        allCertificatesAreValid = allCertificatesAreValid && certificate.getId() > 0
                                && certificate.getId() < 3;
                    }
                    return !nameIsValid || !allCertificatesAreValid;
                }
        ));

        return tagDAO;
    }

    @Test
    void getAllTagsTest() {
        Assertions.assertEquals(allTags, tagService.getAllTags(null, null));
    }

    @Test
    void getTagByIdPositive() {
        Tag tag = tagService.getTagById(1);
        Assertions.assertEquals(tag, tag1);
    }

    @Test
    void getTagByIdInvalidId() {
        Assertions.assertThrows(ResourceNotFoundServiceException.class, () -> tagService.getTagById(3));
    }

    @Test
    void insertTagWithDuplicateName() {
        Assertions.assertThrows(ResourceConflictServiceException.class,
                () -> tagService.insertTag(new Tag(5, "name1")));
    }

    @Test
    void deleteTagByIdPositive() {
        tagService.deleteTagById(2);
    }

    @Test
    void deleteTagByIdInvalidId() {
        Assertions.assertThrows(InvalidRequestDataServiceException.class, () -> tagService.deleteTagById(3));
    }

    @Test
    void insertTagDuplicateName() {
        Assertions.assertThrows(ResourceConflictServiceException.class, () -> tagService.insertTag(new Tag(3, "name1")));
    }

}