package com.epam.esm.schepov.persistence.dao;

/**
 * The enum contains objects which represent the names of columns
 * in the database tables.
 *
 */
public enum Column {
    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    CREATE_DATE("create_date"),
    LAST_UPDATE_DATE("last_update_date"),
    DURATION("duration"),
    CERTIFICATES_ID("cs_id"),
    CERTIFICATES_NAME("cs_name"),
    TAGS_ID("t_id"),
    TAG_NAME("t_name"),
    CERTIFICATE_ID("certificate_id");


    private final String name;

    Column(String name){
        this.name = name;
    }

    /**
     * Returns the name of the column as a {@link String} object.
     *
     * @return The name of the column.
     */
    public String getName() {
        return name;
    }

}
