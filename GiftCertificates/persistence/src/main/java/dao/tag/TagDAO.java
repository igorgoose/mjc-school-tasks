package dao.tag;


import com.epam.esm.schepov.core.entity.Tag;

import java.util.Set;

public interface TagDAO {
    Set<Tag> getAll();
    Tag getById(int id);
    void insertTag(Tag tag);
    void deleteTag(int id);
}
