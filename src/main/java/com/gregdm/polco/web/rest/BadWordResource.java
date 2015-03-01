package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.BadWord;
import com.gregdm.polco.repository.BadWordRepository;
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
 * REST controller for managing BadWord.
 */
@RestController
@RequestMapping("/api")
public class BadWordResource {

    private final Logger log = LoggerFactory.getLogger(BadWordResource.class);

    @Inject
    private BadWordRepository badWordRepository;

    /**
     * POST  /badWords -> Create a new badWord.
     */
    @RequestMapping(value = "/badWords",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody BadWord badWord) throws URISyntaxException {
        log.debug("REST request to save BadWord : {}", badWord);
        if (badWord.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new badWord cannot already have an ID").build();
        }
        badWordRepository.save(badWord);
        return ResponseEntity.created(new URI("/api/badWords/" + badWord.getId())).build();
    }

    /**
     * PUT  /badWords -> Updates an existing badWord.
     */
    @RequestMapping(value = "/badWords",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody BadWord badWord) throws URISyntaxException {
        log.debug("REST request to update BadWord : {}", badWord);
        if (badWord.getId() == null) {
            return create(badWord);
        }
        badWordRepository.save(badWord);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /badWords -> get all the badWords.
     */
    @RequestMapping(value = "/badWords",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BadWord> getAll() {
        log.debug("REST request to get all BadWords");
        return badWordRepository.findAll();
    }

    /**
     * GET  /badWords/:id -> get the "id" badWord.
     */
    @RequestMapping(value = "/badWords/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BadWord> get(@PathVariable Long id) {
        log.debug("REST request to get BadWord : {}", id);
        return Optional.ofNullable(badWordRepository.findOne(id))
            .map(badWord -> new ResponseEntity<>(
                badWord,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /badWords/:id -> delete the "id" badWord.
     */
    @RequestMapping(value = "/badWords/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete BadWord : {}", id);
        badWordRepository.delete(id);
    }
}
