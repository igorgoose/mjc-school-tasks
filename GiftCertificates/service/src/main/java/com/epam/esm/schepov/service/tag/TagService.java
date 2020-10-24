package com.epam.esm.schepov.service.tag;

import com.epam.esm.schepov.core.entity.Tag;

import java.util.Set;

public interface TagService {
 Set<Tag> getAllTags();
 Tag getTagById(int id);
}
