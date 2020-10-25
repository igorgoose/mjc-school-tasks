package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
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
        return tagDAO.getAllTags();
    }

    @Override
    public Tag getTagById(int id) {
        return tagDAO.getTagById(id);
    }

    @Override
    public void deleteTagById(int id) {
        tagDAO.deleteTag(id);
    }

    @Override
    public void insertTag(Tag tag) {
        tagDAO.insertTag(tag);
    }

}
