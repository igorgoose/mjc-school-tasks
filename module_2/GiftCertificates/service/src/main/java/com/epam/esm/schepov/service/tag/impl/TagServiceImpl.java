package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }


    @Override
    public Set<Tag> getAllTags() {
        return tagDAO.getAll();
    }

    @Override
    public Tag getTagById(int id) throws ResourceNotFoundServiceException {
        Tag tag = tagDAO.getById(id);
        if (tag == null) {
            throw new ResourceNotFoundServiceException("Queried tag doesn't exist(id=" + id + ")");
        }
        return tag;
    }

    @Override
    public void deleteTagById(int id) throws InvalidRequestDataServiceException {
        if (null == tagDAO.getById(id)) {
            throw new InvalidRequestDataServiceException("Queried tag doesn't exist(id=" + id + ")");
        }
        tagDAO.delete(id);
    }

    @Override
    public Tag insertTag(Tag tag) throws ResourceConflictServiceException {
        Tag persistedTag = tagDAO.getByName(tag.getName());
        if (persistedTag == null) {
            tagDAO.insert(tag);
            return tagDAO.getByName(tag.getName());
        } else {
            throw new ResourceConflictServiceException("Tag with name " + tag.getName() + " already exists.");
        }
    }

}
