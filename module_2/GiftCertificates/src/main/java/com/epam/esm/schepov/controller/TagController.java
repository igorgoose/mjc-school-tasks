package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.error.Error;
import com.epam.esm.schepov.error.ErrorCodeCreator;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@Controller
@RequestMapping(value = "/tags", produces = "application/json")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService, ErrorCodeCreator errorCodeCreator) {
        this.tagService = tagService;
    }

    @GetMapping
    @ResponseBody
    public Set<Tag> all() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Tag one(@PathVariable("id") int id) {
        return tagService.getTagById(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Tag> create(@RequestBody Tag tag, UriComponentsBuilder uriComponentsBuilder) {
        Tag createdTag = tagService.insertTag(tag);
        URI locationUri = uriComponentsBuilder
                .path("/tags/")
                .path(String.valueOf(createdTag.getId()))
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(createdTag, headers, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        tagService.deleteTagById(id);
    }

    @ExceptionHandler(ResourceNotFoundServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error resourceNotFound(ResourceNotFoundServiceException exception){
        return new Error(40, exception.getMessage());
    }

    @ExceptionHandler(ResourceConflictServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Error resourceConflict(ResourceConflictServiceException exception){
        return new Error(41, exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestDataServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error resourceConflict(InvalidRequestDataServiceException exception){
        return new Error(40, exception.getMessage());
    }

}
