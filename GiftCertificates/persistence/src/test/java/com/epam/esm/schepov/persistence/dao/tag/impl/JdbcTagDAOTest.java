package com.epam.esm.schepov.persistence.dao.tag.impl;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

class JdbcTagDAOTest {


    private static EmbeddedDatabase database;
    private static TagDAO tagDAO;

    @BeforeAll
    public static void setUp(){
        database = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("create_tables.sql", "initialize_data.sql")
                .build();
        tagDAO = new JdbcTagDAO(new JdbcTemplate(database));
    }

    @Test
    public void getTagByIdTestPositive(){
        Tag tag = new Tag(1, "extreme");
        assertEquals(tagDAO.getById(1), tag);
    }

    @Test
    public void getTagByNameTestPositive(){
        Tag tag = new Tag(1, "extreme");
        assertEquals(tagDAO.getByName("extreme"), tag);
    }



}