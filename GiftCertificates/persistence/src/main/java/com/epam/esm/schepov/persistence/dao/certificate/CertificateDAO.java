package com.epam.esm.schepov.persistence.dao.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;

import java.util.Set;

public interface CertificateDAO {
    Set<GiftCertificate> getAll();

    GiftCertificate getById(int id);

    GiftCertificate getByName(String name);

    void insert(GiftCertificate giftCertificate);

    void delete(int id);

    void update(int id, GiftCertificate giftCertificate);
}
