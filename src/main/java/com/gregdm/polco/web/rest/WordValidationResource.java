package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.repository.WordValidationRepository;
import com.gregdm.polco.service.WordValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WordValidation.
 */
@RestController
@RequestMapping("/api")
public class WordValidationResource {

    private final Logger log = LoggerFactory.getLogger(WordValidationResource.class);

    @Inject
    private WordValidationRepository wordValidationRepository;

    @Inject
    private WordValidationService wordValidationService;

    /**
     * POST  /wordValidations -> Create a new wordValidation.
     */
    @RequestMapping(value = "/wordValidations",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody WordValidation wordValidation) throws URISyntaxException {
        log.debug("REST request to save WordValidation : {}", wordValidation);
        if (wordValidation.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new wordValidation cannot already have an ID").build();
        }
        wordValidationRepository.save(wordValidation);
        return ResponseEntity.created(new URI("/api/wordValidations/" + wordValidation.getId())).build();
    }

    /**
     * PUT  /wordValidations -> Updates an existing wordValidation.
     */
    @RequestMapping(value = "/wordValidations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody WordValidation wordValidation) throws URISyntaxException {
        log.debug("REST request to update WordValidation : {}", wordValidation);
        if (wordValidation.getId() == null) {
            return create(wordValidation);
        }
        wordValidationRepository.save(wordValidation);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /wordValidations -> get all the wordValidations.
     */
    @RequestMapping(value = "/wordValidations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WordValidation> getAll() {
        log.debug("REST request to get all WordValidations");
        return wordValidationRepository.findAll();
    }

    /**
     * GET  /wordValidations/:id -> get the "id" wordValidation.
     */
    @RequestMapping(value = "/wordValidations/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WordValidation> get(@PathVariable Long id) {
        log.debug("REST request to get WordValidation : {}", id);
        return Optional.ofNullable(wordValidationRepository.findOne(id))
            .map(wordValidation -> new ResponseEntity<>(
                wordValidation,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /wordValidations/:id -> get the "id" wordValidation.
     */
    @RequestMapping(value = "/wordValidations/validate/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public boolean validate(@PathVariable Long id) {
        log.debug("REST request to get WordValidation : {}", id);
        return this.wordValidationService.validate(wordValidationRepository.findOne(id));
    }

    /**
     * DELETE  /wordValidations/:id -> delete the "id" wordValidation.
     */
    @RequestMapping(value = "/wordValidations/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete WordValidation : {}", id);
        wordValidationRepository.delete(id);
    }
}
