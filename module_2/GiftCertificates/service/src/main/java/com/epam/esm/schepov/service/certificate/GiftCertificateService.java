package com.epam.esm.schepov.service.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;

import java.util.Set;

public interface GiftCertificateService {

    Set<GiftCertificate> getAllCertificates(String sortParameter, String orderParameter);


    GiftCertificate getCertificateById(int id) throws ResourceNotFoundServiceException;


    GiftCertificate insertCertificate(GiftCertificate giftCertificate)
            throws ResourceConflictServiceException, InvalidEntityDataServiceException;

    GiftCertificate updateCertificate(int id, GiftCertificate giftCertificate)
            throws InvalidRequestDataServiceException, InvalidEntityDataServiceException,
            ResourceConflictServiceException;

    void deleteCertificate(int id) throws InvalidRequestDataServiceException;


}
