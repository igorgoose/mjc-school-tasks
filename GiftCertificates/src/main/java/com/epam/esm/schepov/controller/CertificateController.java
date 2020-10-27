package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.CertificateTag;
import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.certificatetag.CertificateTagService;
import com.epam.esm.schepov.service.exception.CertificateServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {

    private final GiftCertificateService giftCertificateService;
    private final TagService tagService;
    private final CertificateTagService certificateTagService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService, TagService tagService,
                                 CertificateTagService certificateTagService) {
        this.giftCertificateService = giftCertificateService;
        this.tagService = tagService;
        this.certificateTagService = certificateTagService;
    }

    @GetMapping
    public Set<GiftCertificate> all() {
        return giftCertificateService.getAllCertificates();
    }

    @GetMapping("/{id}")
    public GiftCertificate one(@PathVariable("id") int id) {
        try {
            return giftCertificateService.getCertificateById(id);
        } catch (CertificateServiceException e) {
           throw new RuntimeException();
        }
    }

//    @GetMapping("/new")
//    public String newCertificate(@ModelAttribute("certificate") GiftCertificate giftCertificate, Model model) {
//        model.addAttribute("tags", tagService.getAllTags());
//        return "certificates/new";
//    }

//    @GetMapping("/{id}/edit")
//    public String edit(@PathVariable("id") int id, Model model) {
//        GiftCertificate certificate = giftCertificateService.getCertificateById(id);
//        model.addAttribute("certificate", certificate);
//        model.addAttribute("tags", tagService.getAllTags());
//        model.addAttribute("oldTags", certificate.getTags());
//        return "certificates/edit";
//    }

    @PostMapping
    public void create(@RequestBody GiftCertificate giftCertificate) {
        try {
            giftCertificate = giftCertificateService.insertCertificate(giftCertificate);
            for (Tag tag : giftCertificate.getTags()) {
                tag = tagService.getTagById(tag.getId());
                certificateTagService.insertCertificateTag(new CertificateTag(giftCertificate.getId(), tag.getId()));
            }
        } catch (Exception e) {

        }
    }

    @PutMapping("/{id}")
    public void update(@RequestBody GiftCertificate giftCertificate, @PathVariable("id") int id) {
        try {
            giftCertificateService.updateCertificate(id, giftCertificate);
            certificateTagService.deleteByCertificateId(id);
            for (Tag tagToAdd : giftCertificate.getTags()) {
                certificateTagService.insertCertificateTag(new CertificateTag(id, tagToAdd.getId()));
            }
        } catch (Exception e){

        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        try {
            giftCertificateService.deleteCertificate(id);
        } catch (CertificateServiceException e) {

        }
    }


}
