package com.epam.esm.schepov.service.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.service.exception.CertificateServiceException;

import java.util.Set;

public interface GiftCertificateService {

    Set<GiftCertificate> getAllCertificates();


    GiftCertificate getCertificateById(int id) throws CertificateServiceException;


    GiftCertificate insertCertificate(GiftCertificate giftCertificate) throws CertificateServiceException;


    void deleteCertificate(int id) throws CertificateServiceException;

    void updateCertificate(int id, GiftCertificate giftCertificate) throws CertificateServiceException;
}
