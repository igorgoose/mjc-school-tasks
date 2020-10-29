package com.epam.esm.schepov.service.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.service.exception.InvalidDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;

import java.util.Set;

public interface GiftCertificateService {

    Set<GiftCertificate> getAllCertificates();


    GiftCertificate getCertificateById(int id) throws ResourceNotFoundServiceException;


    GiftCertificate insertCertificate(GiftCertificate giftCertificate)
            throws ResourceConflictServiceException, InvalidDataServiceException;

    GiftCertificate updateCertificate(int id, GiftCertificate giftCertificate)
            throws ResourceNotFoundServiceException, InvalidDataServiceException;

    void deleteCertificate(int id) throws ResourceNotFoundServiceException;


}
