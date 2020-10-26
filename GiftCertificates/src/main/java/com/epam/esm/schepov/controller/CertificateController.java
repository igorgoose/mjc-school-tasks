package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.certificatetag.CertificateTagService;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/certificates", produces = {"text/html; charset=UTF-8"})
public class CertificateController {

    private final GiftCertificateService giftCertificateService;
    private final TagService tagService;
    private final CertificateTagService certificateTagService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService, TagService tagService, CertificateTagService certificateTagService) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
        this.certificateTagService = certificateTagService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("certificates", giftCertificateService.getAllCertificates());
        return "certificates/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        GiftCertificate certificate = giftCertificateService.getCertificateById(id);
        System.out.println(certificate.getTags());
        model.addAttribute("certificate", certificate);
        return "certificates/show";
    }

    @GetMapping("/new")
    public String newCertificate(@ModelAttribute("certificate") GiftCertificate giftCertificate, Model model) {
        model.addAttribute("tags", tagService.getAllTags());
        return "certificates/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        GiftCertificate certificate = giftCertificateService.getCertificateById(id);
        model.addAttribute("certificate", certificate);
        model.addAttribute("tags", tagService.getAllTags());
        model.addAttribute("selectedTags", certificate.getTags());
        return "certificates/edit";
    }

    @PostMapping
    public String create(@ModelAttribute("certificate") GiftCertificate giftCertificate,
                         @RequestParam("selected_tags") int[] tagIds){
        giftCertificate = giftCertificateService.insertCertificate(giftCertificate);
        Tag tag;
        for (int tagId : tagIds) {
            //todo process future exceptions
            System.out.println(tagId);
            tag = tagService.getTagById(tagId);
            certificateTagService.insertCertificateTag(giftCertificate.getId(), tag.getId());
        }
        return "redirect:/certificates";
    }

    @PutMapping("/{id}")
    public String update(@ModelAttribute("certificate") GiftCertificate giftCertificate,
                         @PathVariable("id") int id){
        giftCertificateService.updateCertificate(id, giftCertificate);
        return "redirect:/certificates";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        giftCertificateService.deleteCertificate(id);
        return "redirect:/certificates";
    }

}
