package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.persistence.dao.certificate.CertificateDAO;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/certificates", produces = {"text/html; charset=UTF-8"})
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("certificates", giftCertificateService.getAllCertificates());
        return "certificates/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("certificate", giftCertificateService.getCertificateById(id));
        return "certificates/show";
    }

    @GetMapping("/new")
    public String newCertificate(@ModelAttribute("certificate") GiftCertificate giftCertificate) {
        return "certificates/new";
    }

    @GetMapping("/edit")
    public String editCertificate(@ModelAttribute("certificate") GiftCertificate giftCertificate) {
        return "certificates/edit";
    }

    @PostMapping
    public String create(@ModelAttribute("certificate") GiftCertificate giftCertificate){
        giftCertificateService.insertCertificate(giftCertificate);
        return "redirect:/certificates";
    }

    @PutMapping
    public String edit(@ModelAttribute("certificate") GiftCertificate giftCertificate){
        //todo implement
        return "redirect:/certificates";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        giftCertificateService.deleteCertificate(id);
        return "redirect:/certificates";
    }

}
