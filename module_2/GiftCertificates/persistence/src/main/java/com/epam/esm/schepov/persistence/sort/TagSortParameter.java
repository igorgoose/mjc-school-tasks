package com.epam.esm.schepov.persistence.sort;

import com.epam.esm.schepov.core.entity.Tag;

/**
 * The enum represents {@link Tag}'s properties which can be used
 * to order tags. Moreover, provides methods {@link #getSortParameter()} and
 * {@link #getSortParameter(boolean)} to get part of an SQL query used to order the result
 * set.
 *
 * @author Igor Schepov
 * @see Tag
 * @since 1.0
 */
public enum TagSortParameter {
    NAME("name");

    private final String name;

    TagSortParameter(String name) {
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
