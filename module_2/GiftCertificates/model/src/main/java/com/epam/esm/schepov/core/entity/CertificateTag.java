package com.epam.esm.schepov.core.entity;

import java.io.Serializable;
import java.util.Objects;

public class CertificateTag implements Serializable {

    private int id;
    private int certificateId;
    private int tagId;

    public CertificateTag(){

    }

    public CertificateTag(int id, int certificateId, int tagId) {
        this.id = id;
        this.certificateId = certificateId;
        this.tagId = tagId;
    }

    public CertificateTag(int certificateId, int tagId) {
        this.certificateId = certificateId;
        this.tagId = tagId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateTag that = (CertificateTag) o;
        return id == that.id &&
                Objects.equals(certificateId, that.certificateId) &&
                Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, certificateId, tagId);
    }
}
