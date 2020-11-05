package com.epam.esm.schepov.persistence.dao.tag.impl;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.persistence.sort.TagSortParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static com.epam.esm.schepov.persistence.dao.Column.*;

/**
 * {@code JdbcTagDAO} provides methods to interact with a database
 * and perform CRD operations on tags. The class utilizes a {@link JdbcOperations}
 * object, hence the name.
 *
 * @author Igor Schepov
 * @see Tag
 * @see TagDAO
 * @see TagSortParameter
 * @since 1.0
 */
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

    /**
     * Injects a {@code JdbcOperations} object which is used to interact with the database.
     *
     * @param jdbcOperations A JdbcOperations object to interact with the database.
     */
    @Autowired
    public JdbcTagDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    /**
     * Returns all the tags stored.
     *
     * @return All tags from the storage.
     */
    @Override
    public Set<Tag> getAll() {
        return jdbcOperations.query(GET_ALL_TAGS, this::mapResultSet);
    }

    /**
     * Returns all the tags stored in order described by {@code sortParameter}
     * and {@code inDescendingOrder}.
     * <p>
     * The parameter {@code sortParameter} represents a property of tags, by which
     * the returned tags are ordered.
     * <p>
     * If the parameter {@code inDescendingOrder} is true, the order of the returned tags
     * is descending.
     *
     * @param sortParameter     A property of tags, by which
     *                          the returned tags are ordered.
     * @param inDescendingOrder If true the order of the returned tags
     *                          is descending.
     * @return All tags from the storage ordered according to the parameters passed.
     */
    @Override
    public Set<Tag> getAll(TagSortParameter sortParameter, boolean inDescendingOrder) {
        return jdbcOperations.query(GET_ALL_TAGS + sortParameter.getSortParameter(inDescendingOrder),
                this::mapResultSet);
    }

    /**
     * Returns the tag with identifier equal to {@code id} passed as a parameter.
     *
     * @param id The identifier of the requested tag.
     * @return The tag with the specified identifier.
     */
    @Override
    public Tag getById(int id) {
        return Objects.requireNonNull(jdbcOperations.query(GET_TAG_BY_ID, this::mapResultSet, id))
                .stream().findAny().orElse(null);
    }

    /**
     * Returns the tag with name equal to {@code name} passed as a parameter.
     *
     * @param name The name of the requested tag.
     * @return The tag with the specified name.
     */
    @Override
    public Tag getByName(String name) {
        return Objects.requireNonNull(jdbcOperations.query(GET_TAG_BY_NAME, this::mapResultSet, name))
                .stream().findAny().orElse(null);
    }

    /**
     * Inserts the passed {@code tag}.
     *
     * @param tag The certificate to insert.
     * @throws DaoException Thrown in case of invalid {@code tag}'s properties.
     */
    @Override
    public void insert(Tag tag) throws DaoException {
        try {
            jdbcOperations.update(INSERT_TAG, tag.getName());
        } catch (DuplicateKeyException exception) {
            throw new DaoException("Tag with name " + tag.getName() + " already exists.", exception);
        }
    }

    /**
     * Deletes the tag with the specified {@code id};
     *
     * @param id The identifier of the tag to be deleted.
     */
    @Override
    public void delete(int id) throws DaoException {
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
        if (certificateName != null) {
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
