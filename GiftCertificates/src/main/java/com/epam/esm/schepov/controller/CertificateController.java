package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateDAO certificateDAO;

    @Autowired
    public CertificateController(CertificateDAO certificateDAO) {
        this.certificateDAO = certificateDAO;
    }

    @GetMapping
    public String viewAllCertificates(Model model){
        model.addAttribute("certificates", certificateDAO.getAllCertificates());
        return "certificates/index";
    }

    @GetMapping("/{id}")
    public String viewCertificateById(@PathVariable("id") int id, Model model){
        model.addAttribute("certificate", certificateDAO.getCertificateById(id));
        return "certificates/show";
    }

}
