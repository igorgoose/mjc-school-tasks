package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.GiftCertificate;
import com.epam.esm.schepov.error.Error;
import com.epam.esm.schepov.service.certificate.GiftCertificateService;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
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

/**
 * The {@code CertificateController} class is an endpoint of the API
 * which allows its users to perform CRUD operations on gift certificates.
 *
 * {@code CertificateController} is accessed by sending request to GiftCertificates/certificates
 * and the response produced by {@code CertificateController} carries application/json
 * type of content(except for {@link #delete} method, which send no content back to the user).
 * This information can be found in {@link RequestMapping} annotation's parameters.
 *
 * As mentioned before, {@code CertificateController} provides the user with methods to create({@link #create}),
 * read({@link #getAll} and {@link #getOne}), update({@link #update}) and delete({@link #delete})
 * gift certificates from storage. This means that any type of request except
 * for GET, POST, PUT, DELETE will by default result in a response with HTTP status 405 - Method
 * Not Allowed.
 *
 * @author Igor Schepov
 * @see  TagController
 * @since 1.0
 *
 */
@RestController
@RequestMapping(value = "/certificates", produces = "application/json")
@Transactional
public class CertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * Returns all the gift certificates in the storage.
     *
     * Annotated by {@link GetMapping} with no parameters. Therefore, processes GET requests at
     * GiftCertificates/certificates.
     *
     * @return All gift certificates in the storage.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Set<GiftCertificate> getAll() {
        return giftCertificateService.getAllCertificates();
    }

    /**
     * Returns the gift certificate with the specified from the storage.
     *
     * Annotated by {@link GetMapping} with parameter value = "/{id}". Therefore, processes GET requests at
     * GiftCertificates/certificates/_id_, where _id_ is the identifier of the requested gift certificate
     * represented by a natural number.
     *
     * If there is no gift certificate with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param id Identifier of the requested gift certificate. Inferred from the request URI.
     * @return Gift certificate with the specified id.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GiftCertificate getOne(@PathVariable("id") int id) {
        return giftCertificateService.getCertificateById(id);
    }

    /**
     * Inserts the gift certificate passed in the request body into the storage.
     *
     * Annotated with{@link PostMapping} with parameter consumes = "application/json",
     * which implies that the method processes POST requests at GiftCertificates/certificates and that the
     * information about the new gift certificate must be carried in request body
     * in JSON format.
     *
     * The default response status is 200 - OK.
     * In case the name of the new gift certificate collides with a stored gift certificate's name response status is
     * 422 - Unprocessable Entity.
     *
     * @param giftCertificate Gift certificate to be inserted into storage. Inferred from the request body.
     * @param uriComponentsBuilder Builder for the URI of the created gift certificate.
     * @return {@link ResponseEntity} with the inserted gift certificate and its location included.
     */
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


    /**
     * Updates the gift certificate in the storage using {@code giftCertificate} passed as a parameter.
     *
     * Annotated with{@link PutMapping} with parameter consumes = "application/json",
     * which implies that the method processes PUT requests at GiftCertificates/certificates/_id_, where
     * _id_ is the identifier of the certificate to be updated represented by a natural number and that the
     * information about the updated gift certificate must be carried in request body
     * in JSON format.
     *
     * The default response status is 200 - OK.
     * In case the name of the updated gift certificate collides with a stored gift certificate's name
     * response status is 422 - Unprocessable Entity.
     * In case the updated gift certificate contains a non-existent tag response status
     * is 422 - Unprocessable Entity.
     *
     * @param giftCertificate Updated value of the gift certificate.
     * @param id The identifier of the gift certificate to be updated. Inferred from the request URI.
     * @return Updated gift certificate.
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GiftCertificate update(@RequestBody GiftCertificate giftCertificate, @PathVariable("id") int id) {
        return giftCertificateService.updateCertificate(id, giftCertificate);
    }

    /**
     * Deletes the gift certificate with the specified id from the storage.
     *
     * Annotated with{@link DeleteMapping} with parameter value = "/{id}",
     * which implies that the method processes DELETE requests at
     * GiftCertificates/certificates/_id_, where _id_ is the identifier of the gift certificate to be deleted
     * represented by a natural number.
     *
     * The default response status is 204 - No Content, as the response body is empty.
     * In case there is no gift certificate with the specified id in the storage response status
     * is 400 - Bad Request.
     *
     * @param id The identifier of the gift certificate to be deleted. Inferred from the request URI.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        giftCertificateService.deleteCertificate(id);
    }

    @ExceptionHandler(ResourceNotFoundServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    private Error resourceNotFound(ResourceNotFoundServiceException exception){
        return new Error(44, exception.getMessage());
    }

    @ExceptionHandler(ResourceConflictServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    private Error resourceConflict(ResourceConflictServiceException exception){
        return new Error(41, exception.getMessage());
    }

    @ExceptionHandler(InvalidEntityDataServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    private Error invalidEntityData(InvalidEntityDataServiceException exception){
        return new Error(42, exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestDataServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    private Error invalidRequestData(InvalidRequestDataServiceException exception){
        return new Error(40, exception.getMessage());
    }

}
