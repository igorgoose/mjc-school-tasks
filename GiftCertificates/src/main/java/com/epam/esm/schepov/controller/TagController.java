package com.epam.esm.schepov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tags")
public class TagController {

    @GetMapping
    public String viewAllTags(){
        return "tags/all_tags";
    }

}
