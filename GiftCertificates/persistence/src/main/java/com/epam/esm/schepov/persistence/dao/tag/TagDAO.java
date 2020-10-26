package com.epam.esm.schepov.persistence.dao.tag;


import com.epam.esm.schepov.core.entity.Tag;

import java.util.Set;

public interface TagDAO {
    Set<Tag> getAll();
    Tag getById(int id);
    Tag getByName(String name);
    void insert(Tag tag);
    void delete(int id);
}
