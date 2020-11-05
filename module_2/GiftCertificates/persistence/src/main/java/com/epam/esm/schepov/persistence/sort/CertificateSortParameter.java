package com.epam.esm.schepov.persistence.sort;

import com.epam.esm.schepov.core.entity.GiftCertificate;

/**
 * The enum represents {@link GiftCertificate}'s properties which can be used
 * to order certificates. Moreover, provides methods {@link #getSortParameter()} and
 * {@link #getSortParameter(boolean)} to get part of an SQL query used to order the result
 * set.
 *
 * @author Igor Schepov
 * @see GiftCertificate
 * @since 1.0
 */
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

    /**
     * Returns the name of the property.
     *
     * @return The name of the property.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a {@link String} containing part of an SQL query used to order the result
     * set.
     *
     * @return Part of an SQL query used to order the result set.
     */
    public String getSortParameter() {
        return " order by " + name;
    }

    /**
     * Returns a {@link String} containing part of an SQL query used to order the result
     * set.
     *
     * @param descendingOrder Indicates if the result set needs to be sorted in descending order.
     * @return Part of an SQL query used to order the result set.
     */
    public String getSortParameter(boolean descendingOrder) {
        return descendingOrder ? getSortParameter() + " desc" : getSortParameter();
    }
}
