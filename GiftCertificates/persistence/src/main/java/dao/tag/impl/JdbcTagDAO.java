package dao.tag.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import dao.tag.TagDAO;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class JdbcTagDAO implements TagDAO {

    private static final String NAME_COLUMN = "name";
    private static final String ID_COLUMN = "id";
    private static final String CERTIFICATE_ID_COLUMN = "cs_id";
    private static final String CERTIFICATE_NAME_COLUMN = "cs_name";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String PRICE_COLUMN = "price";
    private static final String CREATE_DATE_COLUMN = "create_date";
    private static final String LAST_UPDATE_DATE_COLUMN = "last_update_date";
    private static final String DURATION_COLUMN = "duration";

    private static final String JOIN_CERTIFICATES_QUERY_PART = "left join certificate_tags ct on tags.id = ct.tag_id " +
            "left join certificates cs on ct.certificate_id = cs.id ";

    private static final String GET_TAG_BY_ID = "select tags.id id, tags.name name, cs.id cs_id, " +
            "cs.name cs_name, description, price, " +
            "create_date, last_update_date, duration " +
            "from tags " + JOIN_CERTIFICATES_QUERY_PART +
            "where tags.id = ?";

    private static final String GET_TAG_BY_NAME = "select tags.id id, tags.name name, cs.id cs_id, " +
            "cs.name cs_name, description, price, " +
            "create_date, last_update_date, duration " +
            "from tags " + JOIN_CERTIFICATES_QUERY_PART +
            "where tags.name = ?";

    private static final String GET_ALL_TAGS = "select tags.id id, tags.name name, cs.id cs_id, " +
            "cs.name cs_name, description, price, " +
            "create_date, last_update_date, duration " +
            "from tags " + JOIN_CERTIFICATES_QUERY_PART;


    private static final String INSERT_TAG = "insert into tags (name) values (?)";
    private static final String DELETE_TAG = "delete from tags where id = ?";

    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcTagDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    @Override
    public Set<Tag> getAllTags() {
        return jdbcOperations.query(GET_ALL_TAGS,
                (resultSet -> {
                    Set<Tag> tags = new LinkedHashSet<>();
                    while (resultSet.next()) {
                        Tag tag = mapTag(resultSet, 0);
                        tags.remove(tag);
                        tags.add(tag);
                    }
                    return tags;
                }));
    }

    @Override
    public Tag getTagById(int id) {
        return jdbcOperations.queryForObject(GET_TAG_BY_ID, this::mapTag, id);
    }

    @Override
    public Tag getTagByName(String name) {
        return jdbcOperations.queryForObject(GET_TAG_BY_NAME, this::mapTag, name);
    }

    @Override
    public void insertTag(Tag tag) {
        //todo process name collision
        jdbcOperations.update(INSERT_TAG, tag.getName());
    }

    @Override
    public void deleteTag(int id) {
        jdbcOperations.update(DELETE_TAG, id);
    }

    private Tag mapTag(ResultSet resultSet, int row) throws SQLException {
        Tag tag = new Tag(resultSet.getInt(ID_COLUMN), resultSet.getString(NAME_COLUMN));
        tag.getGiftCertificates().add(new GiftCertificate(
                resultSet.getInt(CERTIFICATE_ID_COLUMN),
                resultSet.getString(CERTIFICATE_NAME_COLUMN),
                resultSet.getString(DESCRIPTION_COLUMN),
                resultSet.getFloat(PRICE_COLUMN),
                resultSet.getTimestamp(CREATE_DATE_COLUMN),
                resultSet.getTimestamp(LAST_UPDATE_DATE_COLUMN),
                resultSet.getInt(DURATION_COLUMN)
        ));
        return tag;
    }



}
