package com.epam.esm.schepov.persistence.dao.certificate;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.persistence.exception.DaoException;

import java.util.Set;

public interface CertificateDAO {
    Set<GiftCertificate> getAll();

    GiftCertificate getById(int id);

    GiftCertificate getByName(String name);

    void insert(GiftCertificate giftCertificate) throws DaoException;

    void delete(int id) throws DaoException;

    void update(int id, GiftCertificate giftCertificate) throws DaoException;
}
