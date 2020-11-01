package com.epam.esm.schepov.controller;

import com.epam.esm.schepov.core.entity.Tag;
import com.epam.esm.schepov.error.Error;
import com.epam.esm.schepov.service.exception.InvalidEntityDataServiceException;
import com.epam.esm.schepov.service.exception.InvalidRequestDataServiceException;
import com.epam.esm.schepov.service.exception.ResourceConflictServiceException;
import com.epam.esm.schepov.service.exception.ResourceNotFoundServiceException;
import com.epam.esm.schepov.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Set;

/**
 * The {@code TagController} class is an endpoint of the API
 * which allows its users to perform CRD operations on tags.
 * <p>
 * {@code TagController} is accessed by sending request to GiftCertificates/tags
 * and the response produced by {@code TagController} carries application/json
 * type of content(except for {@link #delete} method, which send no content back to the user).
 * This information can be found in {@link RequestMapping} annotation's parameters.
 * <p>
 * As mentioned before, {@code TagController} provides the user with methods to create({@link #create}),
 * read({@link #getAll} and {@link #getOne}) and delete({@link #delete}) tags from storage. This means that
 * any type of request except for GET, POST, DELETE will by default result in a response with
 * HTTP status 405 - Method Not Allowed.
 *
 * @author Igor Schepov
 * @see CertificateController
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/tags", produces = "application/json")
public class TagController {

    private final TagService tagService;


    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Returns all the tags in the storage.
     * <p>
     * Annotated by {@link GetMapping} with no parameters. Therefore, processes GET requests at
     * GiftCertificates/tags.
     *
     * @return All tags in the storage.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Set<Tag> getAll(@RequestParam(value = "sort", required = false) String sortParameter,
                           @RequestParam(value = "order", required = false) String orderParameter) {
        return tagService.getAllTags(sortParameter, orderParameter);
    }

    /**
     * Returns the tag with the specified id from the storage.
     * <p>
     * Annotated with {@link GetMapping} with parameter value = "/{id}". Therefore, processes GET requests at
     * GiftCertificates/tags/_id_, where _id_ is the identifier of the requested tag represented by a
     * natural number.
     * <p>
     * If there is no tag with the specified id response gets status 404 - Not Found.
     * The default response status is 200 - OK.
     *
     * @param id Identifier of the requested tag. Inferred from the request URI.
     * @return Tag with the specified id.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Tag getOne(@PathVariable("id") int id) {
        return tagService.getTagById(id);
    }

    /**
     * Inserts the tag passed in the request body into the storage.
     * <p>
     * Annotated with {@link PostMapping} with parameter consumes = "application/json",
     * which implies that the method processes POST requests at GiftCertificates/tags and that the
     * information about the new tag must be carried in request body
     * in JSON format.
     * <p>
     * The default response status is 200 - OK.
     * In case the name of the new tag collides with a stored tag's name response status is
     * 422 - Unprocessable Entity.
     *
     * @param tag                  Tag to be inserted into storage. Inferred from the request body.
     * @param uriComponentsBuilder Builder for the URI of the created tag.
     * @return {@link ResponseEntity} with the inserted tag and its location included.
     */
    @PostMapping(consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Tag> create(@RequestBody Tag tag, UriComponentsBuilder uriComponentsBuilder) {
        Tag createdTag = tagService.insertTag(tag);
        URI locationUri = uriComponentsBuilder
                .path("/tags/")
                .path(String.valueOf(createdTag.getId()))
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(createdTag, headers, HttpStatus.CREATED);
    }


    /**
     * Deletes the tag with the specified id from the storage.
     * <p>
     * Annotated with {@link DeleteMapping} with parameter value = "/{id}",
     * which implies that the method processes DELETE requests at
     * GiftCertificates/tags/_id_, where _id_ is the identifier of the tag to be deleted
     * represented by a natural number.
     * <p>
     * The default response status is 204 - No Content, as the response body is empty.
     * In case there is no tag with the specified id in the storage response status
     * is 400 - Bad Request.
     *
     * @param id The identifier of the tag to be deleted. Inferred from the request URI.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        tagService.deleteTagById(id);
    }

    @ExceptionHandler(ResourceNotFoundServiceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    private Error resourceNotFound(ResourceNotFoundServiceException exception) {
        return new Error(44, exception.getMessage());
    }

    @ExceptionHandler(ResourceConflictServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    private Error resourceConflict(ResourceConflictServiceException exception) {
        return new Error(41, exception.getMessage());
    }

    @ExceptionHandler(InvalidRequestDataServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    private Error invalidRequestData(InvalidRequestDataServiceException exception) {
        return new Error(40, exception.getMessage());
    }

    @ExceptionHandler(InvalidEntityDataServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    private Error invalidEntityData(InvalidEntityDataServiceException exception) {
        return new Error(42, exception.getMessage());
    }

}
