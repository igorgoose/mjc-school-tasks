package com.epam.esm.schepov.service.tag;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.exception.TagServiceException;

import java.util.Set;

public interface TagService {
    Set<Tag> getAllTags();

    Tag getTagById(int id) throws TagServiceException;

    void deleteTagById(int id) throws TagServiceException;

    Tag insertTag(Tag tag) throws TagServiceException;

}
