package com.epam.esm.schepov.service.tag.impl;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.tag.TagDAO;
import com.epam.esm.schepov.persistence.exception.DaoException;
import com.epam.esm.schepov.service.exception.TagServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.LinkedHashSet;
import java.util.Set;

class TagServiceImplTest {


    private static TagService tagService;
    private static final Tag tag1;
    private static final Tag tag2;
    private static final Set<Tag> allTags;

    static {
        tag1 = new Tag(1, "name1");
        tag2 = new Tag(2, "name2");
        allTags = new LinkedHashSet<>();
        allTags.add(tag1);
        allTags.add(tag2);
    }

    @BeforeAll
    static void setUp() {
        TagDAO tagDAO = Mockito.mock(TagDAO.class);
        allTags.add(tag1);
        allTags.add(tag2);
        Mockito.when(tagDAO.getAll()).thenReturn(allTags);

        Mockito.when(tagDAO.getById(1)).thenReturn(tag1);
        Mockito.when(tagDAO.getById(2)).thenReturn(tag2);
        Mockito.when(tagDAO.getById(Mockito.intThat(arg -> arg != 1 && arg != 2))).thenReturn(null);

        Mockito.when(tagDAO.getByName("name1")).thenReturn(tag1);
        Mockito.when(tagDAO.getByName("name2")).thenReturn(tag2);
        Mockito.when(tagDAO.getByName(Mockito.argThat(arg -> !(arg.equals("name1") || arg.equals("name2")))))
                .thenReturn(null);

        Mockito.doNothing().when(tagDAO).delete(Mockito.intThat(arg -> arg == 1 || arg == 2));
        Mockito.doThrow(new DaoException()).when(tagDAO).delete(Mockito.intThat(arg -> !(arg == 1 || arg == 2)));

        Mockito.doNothing().when(tagDAO).insert(Mockito.argThat(arg ->
                        !("name1".equals(arg.getName()) || "name2".equals(arg.getName()))));

        Mockito.doThrow(new DaoException()).when(tagDAO).insert(Mockito.argThat(arg ->
                        ("name1".equals(arg.getName()) || "name2".equals(arg.getName()))));

        tagService = new TagServiceImpl(tagDAO);

    }

    @Test
    void getAllTagsTest() {
        Set<Tag> tags = tagService.getAllTags();
        Assertions.assertEquals(tags, allTags);
    }

    @Test
    void getTagByIdPositive() throws TagServiceException {
        Tag tag = tagService.getTagById(1);
        Assertions.assertEquals(tag, tag1);
    }

    @Test
    void getTagByIdInvalidId() {
        Assertions.assertThrows(TagServiceException.class, () -> tagService.getTagById(3));
    }

    @Test
    void deleteTagByIdPositive() throws TagServiceException {
        tagService.deleteTagById(2);
    }

    @Test
    void deleteTagByIdInvalidId() {
        Assertions.assertThrows(TagServiceException.class, () -> tagService.deleteTagById(3));
    }

    @Test
    void insertTagPositive() throws TagServiceException {
        tagService.insertTag(new Tag(3, "name3"));
    }

    @Test
    void insertTagDuplicateName() {
        Assertions.assertThrows(TagServiceException.class, () ->tagService.insertTag(new Tag(3, "name1")));
    }

}