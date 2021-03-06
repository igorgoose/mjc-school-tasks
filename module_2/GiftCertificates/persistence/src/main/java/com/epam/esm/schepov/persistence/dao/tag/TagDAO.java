package com.epam.esm.schepov.persistence.dao.tag;


import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.persistence.sort.TagSortParameter;

import java.util.Set;

public interface TagDAO {
    Set<Tag> getAll();

    Set<Tag> getAll(TagSortParameter sortParameter, boolean inDescendingOrder);

    Tag getById(int id);

    Tag getByName(String name);

    void insert(Tag tag) throws DaoException;

    void delete(int id) throws DaoException;
}
