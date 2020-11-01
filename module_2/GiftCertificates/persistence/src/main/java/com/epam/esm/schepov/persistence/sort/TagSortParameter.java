package com.epam.esm.schepov.persistence.sort;

public enum TagSortParameter {
    NAME("name");

    private final String name;

    TagSortParameter(String name) {
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
