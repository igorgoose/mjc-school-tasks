package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }


}
