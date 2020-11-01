package com.epam.esm.schepov.service.tag;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;

import java.util.Set;

public interface TagService {
    Set<Tag> getAllTags(String sortParameter, String orderParameter);

    Tag getTagById(int id) throws ResourceNotFoundServiceException;

    void deleteTagById(int id) throws InvalidRequestDataServiceException;

    Tag insertTag(Tag tag) throws ResourceConflictServiceException;


}
