package com.epam.esm.schepov.persistence.dao.tag;


import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.impl.JdbcTagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.persistence.sort.TagSortParameter;

import java.util.Set;

/**
 * An interface which describes CRD methods that its implementations provide
 * to work with {@code Tags} in a storage.
 *
 * @author Igor Schepov
 * @see Tag
 * @see JdbcTagDAO
 * @see TagSortParameter
 * @since 1.0
 */
public interface TagDAO {

    /**
     * Description of a method which returns
     * all the tags stored.
     *
     * @return All tags from the storage.
     */
    Set<Tag> getAll();

    /**
     * Description of a method which returns
     * all the tags stored in order described by {@code sortParameter}
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
    Set<Tag> getAll(TagSortParameter sortParameter, boolean inDescendingOrder);

    /**
     * Description of a method which returns the tag
     * with identifier equal to {@code id} passed as a parameter.
     *
     * @param id The identifier of the requested tag.
     * @return The tag with the specified identifier.
     */
    Tag getById(int id);

    /**
     * Description of a method which returns the tag
     * with name equal to {@code name} passed as a parameter.
     *
     * @param name The name of the requested tag.
     * @return The tag with the specified name.
     */
    Tag getByName(String name);

    /**
     * Description of a method which inserts the passed {@code tag}.
     *
     * @param tag The certificate to insert.
     * @throws DaoException Thrown in case of invalid {@code tag}'s properties.
     */
    void insert(Tag tag) throws DaoException;

    /**
     * Description of a method which deletes the tag with the specified {@code id};
     *
     * @param id The identifier of the tag to be deleted.
     */
    void delete(int id);
}
