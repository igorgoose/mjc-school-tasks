package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.error.Error;
import com.epam.esm.schepov.error.ErrorCodeCreator;
import com.epam.esm.schepov.service.exception.TagServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping(value = "/tags", produces = "application/json")
public class TagController {

    private final TagService tagService;
    private final ErrorCodeCreator errorCodeCreator;

    @Autowired
    public TagController(TagService tagService, ErrorCodeCreator errorCodeCreator) {
        this.tagService = tagService;
        this.errorCodeCreator = errorCodeCreator;
    }

    @GetMapping
    public Set<Tag> all(){
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable("id") int id){
        try {
            Tag tag = tagService.getTagById(id);
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } catch (TagServiceException e) {
            return new ResponseEntity<>(new Error(e.getMessage(), errorCodeCreator.createErrorCode(404, id)),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody Tag tag){
        try {
            return new ResponseEntity<>(tagService.insertTag(tag), HttpStatus.OK);
        } catch (TagServiceException e) {
            return new ResponseEntity<>(new Error(e.getMessage(), 409),
                    HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping(value = "/{id}", consumes = "application/json")
    public void delete(@PathVariable("id") int id){
        try {
            tagService.deleteTagById(id);
        } catch (TagServiceException e) {
            e.printStackTrace();
        }
    }

}
