package com.epam.esm.schepov.persistence.dao.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;

import java.util.Set;

public interface CertificateDAO {
    Set<GiftCertificate> getAllCertificates();

    GiftCertificate getCertificateById(int id);

    GiftCertificate getCertificateByName(String name);

    void insertCertificate(GiftCertificate giftCertificate);

    void deleteCertificate(int id);

    void updateCertificate(int id, GiftCertificate giftCertificate);
}
