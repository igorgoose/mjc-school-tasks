package com.epam.esm.schepov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/", produces = {"text/html; charset=UTF-8"})
    public String getHomePage(){
        return "index";
    }

}
