package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.error.Error;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.InvalidDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(value = "/certificates")
@Transactional
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Set<GiftCertificate> all() {
        return giftCertificateService.getAllCertificates();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GiftCertificate one(@PathVariable("id") int id) {
        return giftCertificateService.getCertificateById(id);
    }

    @PostMapping(consumes = "application/json")
    @ResponseBody
    public ResponseEntity<GiftCertificate> create(@RequestBody GiftCertificate giftCertificate,
                                                  UriComponentsBuilder uriComponentsBuilder) {
        giftCertificate = giftCertificateService.insertCertificate(giftCertificate);
        URI locationUri = uriComponentsBuilder
                .path("/certificates/")
                .path(String.valueOf(giftCertificate.getId()))
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(giftCertificate, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GiftCertificate update(@RequestBody GiftCertificate giftCertificate, @PathVariable("id") int id) {
        return giftCertificateService.updateCertificate(id, giftCertificate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        giftCertificateService.deleteCertificate(id);
    }

    @ExceptionHandler(ResourceNotFoundServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error resourceNotFound(ResourceNotFoundServiceException exception){
        return new Error(40, exception.getMessage());
    }

    @ExceptionHandler(ResourceConflictServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Error resourceConflict(ResourceConflictServiceException exception){
        return new Error(41, exception.getMessage());
    }

    @ExceptionHandler(InvalidDataServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Error invalidData(InvalidDataServiceException exception){
        return new Error(42, exception.getMessage());
    }

}
