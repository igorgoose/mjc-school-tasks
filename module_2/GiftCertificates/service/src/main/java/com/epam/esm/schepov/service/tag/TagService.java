package com.epam.esm.schepov.service.tag;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import com.epam.esm.schepov.service.tag.impl.TagServiceImpl;

import java.util.Set;

/**
 * Provides functionality to process and validate data passed from an endpoint
 * and to send queries to DAO layer.
 *
 * @author Igor Schepov
 * @see TagServiceImpl
 * @see Tag
 * @since 1.0
 */
public interface TagService {

    /**
     * Retrieves all tags stored.
     *
     * @param sortParameter  Tags's property by which the queried tags are sorted.
     * @param orderParameter Order of the queried tags(ascending or descending).
     * @return All tags stored.
     */
    Set<Tag> getAllTags(String sortParameter, String orderParameter);

    /**
     * Retrieves the tag with the specified id.
     *
     * @param id The identifier of the queried tag.
     * @return The tag with the specified id.
     * @throws ResourceNotFoundServiceException If the queried tag does not exist.
     */
    Tag getTagById(int id) throws ResourceNotFoundServiceException;

    /**
     * Deletes the certificate with the specified id from the storage.
     *
     * @param id The id of the certificate to delete.
     * @throws InvalidRequestDataServiceException If the id is invalid.
     */
    void deleteTagById(int id) throws InvalidRequestDataServiceException;

    /**
     * Inserts the passed certificate.
     *
     * @param tag The tag to insert.
     * @return The new tag from storage.
     * @throws ResourceConflictServiceException  If there's a conflict between passed
     *                                           and persisted tags.
     * @throws InvalidEntityDataServiceException If there's an invalid property value
     *                                           inside the passed tag.
     */
    Tag insertTag(Tag tag) throws ResourceConflictServiceException, InvalidEntityDataServiceException;

}
