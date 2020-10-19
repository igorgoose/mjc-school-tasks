package dao.tag.impl;

import com.epam.esm.schepov.core.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import dao.tag.TagDAO;

import java.util.LinkedHashSet;
import java.util.Set;

@Repository
public class JdbcTagDAO implements TagDAO {

    private static final String NAME_COLUMN = "name";
    private static final String ID_COLUMN = "id";
    private static final String GET_TAG_BY_ID = "select id, name from tags where id = ?";
    private static final String GET_ALL_TAGS = "select id, name from tags";
    private static final String INSERT_TAG = "insert into tags (name) values (?)";
    private static final String DELETE_TAG = "delete from tags where id = ?";

    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcTagDAO(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }


    @Override
    public Set<Tag> getAll() {
        return jdbcOperations.query(GET_ALL_TAGS,
                (rs -> {
                    Set<Tag> result = new LinkedHashSet<>();
                    while (rs.next()) {
                        result.add(new Tag(rs.getInt(ID_COLUMN), rs.getString(NAME_COLUMN)));
                    }
                    return result;
                }));
    }

    @Override
    public Tag getById(int id) {
        return jdbcOperations.queryForObject(GET_TAG_BY_ID,
                ((rs, rowNum) -> new Tag(rs.getInt(ID_COLUMN), rs.getString(NAME_COLUMN))),
                id);
    }

    @Override
    public void insertTag(Tag tag) {
        //todo validation
        jdbcOperations.update(INSERT_TAG, tag.getName());
    }

    @Override
    public void deleteTag(int id) {
        jdbcOperations.update(DELETE_TAG, id);
    }


}
