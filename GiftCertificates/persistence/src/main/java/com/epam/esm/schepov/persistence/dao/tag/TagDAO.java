package com.epam.esm.schepov.persistence.dao.tag;


import com.epam.esm.schepov.core.entity.Tag;

import java.util.Set;

public interface TagDAO {
    Set<Tag> getAllTags();
    Tag getTagById(int id);
    Tag getTagByName(String name);
    void insertTag(Tag tag);
    void deleteTag(int id);
}
