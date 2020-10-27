package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.service.tag.TagService;
import com.epam.esm.schepov.service.exception.TagServiceException;
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
    public Tag getTagById(int id) throws TagServiceException {
        Tag tag = tagDAO.getById(id);
        if (tag == null) {
            throw new TagServiceException("Queried tag doesn't exist.");
        }
        return tag;
    }

    @Override
    public void deleteTagById(int id) throws TagServiceException {
        getTagById(id);
        tagDAO.delete(id);
    }

    @Override
    public void insertTag(Tag tag) throws TagServiceException {
        Tag persistedTag = tagDAO.getByName(tag.getName());
        if (persistedTag == null) {
            tagDAO.insert(tag);
        } else {
            throw new TagServiceException("Tag with name " + tag.getName() + " already exists.");
        }
    }

}
