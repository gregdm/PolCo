package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.repository.VerbRepository;
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
 * REST controller for managing Verb.
 */
@RestController
@RequestMapping("/api")
public class VerbResource {

    private final Logger log = LoggerFactory.getLogger(VerbResource.class);

    @Inject
    private VerbRepository verbRepository;

    /**
     * POST  /verbs -> Create a new verb.
     */
    @RequestMapping(value = "/verbs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Verb verb) throws URISyntaxException {
        log.debug("REST request to save Verb : {}", verb);
        if (verb.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new verb cannot already have an ID").build();
        }
        verbRepository.save(verb);
        return ResponseEntity.created(new URI("/api/verbs/" + verb.getId())).build();
    }

    /**
     * PUT  /verbs -> Updates an existing verb.
     */
    @RequestMapping(value = "/verbs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Verb verb) throws URISyntaxException {
        log.debug("REST request to update Verb : {}", verb);
        if (verb.getId() == null) {
            return create(verb);
        }
        verbRepository.save(verb);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /verbs -> get all the verbs.
     */
    @RequestMapping(value = "/verbs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Verb> getAll() {
        log.debug("REST request to get all Verbs");
        return verbRepository.findAll();
    }

    /**
     * GET  /verbs/:id -> get the "id" verb.
     */
    @RequestMapping(value = "/verbs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Verb> get(@PathVariable Long id) {
        log.debug("REST request to get Verb : {}", id);
        return Optional.ofNullable(verbRepository.findOne(id))
            .map(verb -> new ResponseEntity<>(
                verb,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /verbs/:id -> delete the "id" verb.
     */
    @RequestMapping(value = "/verbs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Verb : {}", id);
        verbRepository.delete(id);
    }
}
