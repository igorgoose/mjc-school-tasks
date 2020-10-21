package dao;

public enum Column {
    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    CREATE_DATE("create_date"),
    LAST_UPDATE_DATE("last_update_date"),
    DURATION("duration"),
    CERTIFICATE_ID("cs_id"),
    CERTIFICATE_NAME("cs_name"),
    TAG_ID("t_id"),
    TAG_NAME("t_name");


    private final String name;

    Column(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
