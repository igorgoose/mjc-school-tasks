package com.epam.esm.schepov.persistence.dao.tag.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.epam.esm.schepov.persistence.dao.Column.*;


@Repository
public class JdbcTagDAO implements TagDAO {

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
    public Set<Tag> getAll() {
        return jdbcOperations.query(GET_ALL_TAGS, this::mapResultSet);
    }

    @Override
    public Tag getById(int id) {
        //todo create custom exceptions
        return jdbcOperations.query(GET_TAG_BY_ID, this::mapResultSet, id)
                .stream().findAny().orElseThrow(() -> new RuntimeException("empty"));
    }

    @Override
    public Tag getByName(String name) {
        return jdbcOperations.query(GET_TAG_BY_NAME, this::mapResultSet, name)
                .stream().findAny().orElseThrow(() -> new RuntimeException("empty"));
    }

    @Override
    public void insert(Tag tag) {
        //todo process name collision
        jdbcOperations.update(INSERT_TAG, tag.getName());
    }

    @Override
    public void delete(int id) {
        jdbcOperations.update(DELETE_TAG, id);
    }

    private Set<Tag> mapResultSet(ResultSet resultSet) throws SQLException {
        Set<Tag> tags = new LinkedHashSet<>();
        while (resultSet.next()) {
            Tag tag = mapTag(resultSet);
            for (Tag tagFromSet : tags) {
                if (tagFromSet.equals(tag)) {
                    tag.getGiftCertificates().addAll(tagFromSet.getGiftCertificates());
                }
            }
            tags.remove(tag);
            tags.add(tag);
        }
        return tags;
    }

    private Tag mapTag(ResultSet resultSet) throws SQLException {
        Tag tag = new Tag(resultSet.getInt(ID.getName()), resultSet.getString(NAME.getName()));
        String certificateName = resultSet.getString(CERTIFICATES_NAME.getName());
        if(certificateName != null) {
            tag.getGiftCertificates().add(new GiftCertificate(
                    resultSet.getInt(CERTIFICATES_ID.getName()),
                    certificateName,
                    resultSet.getString(DESCRIPTION.getName()),
                    resultSet.getFloat(PRICE.getName()),
                    resultSet.getTimestamp(CREATE_DATE.getName()),
                    resultSet.getTimestamp(LAST_UPDATE_DATE.getName()),
                    resultSet.getInt(DURATION.getName())
            ));
        }
        return tag;
    }



}
