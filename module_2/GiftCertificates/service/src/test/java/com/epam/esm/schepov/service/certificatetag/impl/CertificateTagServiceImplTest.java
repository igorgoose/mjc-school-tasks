package com.epam.esm.schepov.service.certificatetag.impl;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.persistence.dao.certificatetag.CertificateTagDAO;
import com.epam.esm.schepov.service.certificatetag.CertificateTagService;
import com.epam.esm.schepov.service.exception.CertificateTagServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CertificateTagServiceImplTest {

    private static CertificateTagService certificateTagService;
    private static final CertificateTag certificateTag1 = new CertificateTag(1, 1, 1);

    @BeforeAll
    static void setUp() {
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

        certificateTagService = new CertificateTagServiceImpl(certificateTagDAO);
    }

    @Test
    void insertCertificateTagPositive() throws CertificateTagServiceException {
        certificateTagService.insertCertificateTag(new CertificateTag(3, 3, 3));
    }

    @Test
    void insertCertificateTagDuplicateCertificateAndTagIds() {
        assertThrows(ResourceConflictServiceException.class,
                () -> certificateTagService.insertCertificateTag(new CertificateTag(0, 1, 1)));
    }

    @Test
    void deleteByCertificateIdPositive() throws CertificateTagServiceException {
        certificateTagService.deleteByCertificateId(8);
    }


}