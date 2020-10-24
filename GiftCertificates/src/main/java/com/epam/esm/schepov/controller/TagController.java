package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public String viewAllTags(Model model){
        model.addAttribute("tags", tagService.getAllTags());
        return "tags/index";
    }

    @GetMapping("/{id}")
    public String viewTag(@PathVariable("id") int id, Model model){
        model.addAttribute("tag", tagService.getTagById(id));
        return "tags/show";
    }

}
