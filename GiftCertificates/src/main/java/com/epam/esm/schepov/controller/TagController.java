package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.exception.TagServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public Set<Tag> all(){
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public Tag one(@PathVariable("id") int id){
        try {
            return tagService.getTagById(id);
        } catch (TagServiceException e) {
            throw new RuntimeException();
        }
    }

//    @GetMapping("/new")
//    public String newTag(@ModelAttribute("tag") Tag tag){
//        return "tags/new";
//    }

    @PostMapping
    public void create(@RequestBody Tag tag){
        try {
            tagService.insertTag(tag);
        } catch (TagServiceException e) {
            e.printStackTrace();
        }
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id){
        try {
            tagService.deleteTagById(id);
        } catch (TagServiceException e) {
            e.printStackTrace();
        }
    }

}
