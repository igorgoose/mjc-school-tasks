package com.epam.esm.schepov.service.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;

import java.util.Set;

public interface GiftCertificateService {

    Set<GiftCertificate> getAllCertificates();


    GiftCertificate getCertificateById(int id);


    GiftCertificate insertCertificate(GiftCertificate giftCertificate);


    void deleteCertificate(int id);

    void updateCertificate(int id, GiftCertificate giftCertificate);
}
