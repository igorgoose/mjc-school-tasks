package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/tags", produces = {"text/html; charset=UTF-8"})
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String index(Model model){
        model.addAttribute("tags", tagService.getAllTags());
        return "tags/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("tag", tagService.getTagById(id));
        return "tags/show";
    }

    @GetMapping("/new")
    public String newTag(@ModelAttribute("tag") Tag tag){
        return "tags/new";
    }

    @PostMapping
    public String create(@ModelAttribute("tag") Tag tag){
        tagService.insertTag(tag);
        return "redirect:/tags";
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        tagService.deleteTagById(id);
        return "redirect:/tags";
    }

}
