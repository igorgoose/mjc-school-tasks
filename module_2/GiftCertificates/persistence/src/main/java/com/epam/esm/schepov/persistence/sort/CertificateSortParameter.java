package com.epam.esm.schepov.persistence.sort;

public enum CertificateSortParameter {
    NAME("name"),
    PRICE("price"),
    CREATE_DATE("create_date"),
    LAST_UPDATE_DATE("last_update_date"),
    DURATION("duration");

    private final String name;

    CertificateSortParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSortParameter() {
        return " order by " + name;
    }

    public String getSortParameter(boolean descendingOrder) {
        return descendingOrder ? getSortParameter() + " desc" : getSortParameter();
    }
}
