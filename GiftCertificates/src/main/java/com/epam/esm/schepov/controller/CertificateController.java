package com.epam.esm.schepov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/certificates")
public class CertificateController {

    @GetMapping
    public String viewAllCertificates(){
        return "certificates/all_certificates";
    }
}
