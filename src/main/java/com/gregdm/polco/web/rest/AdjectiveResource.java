package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.repository.AdjectiveRepository;
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
 * REST controller for managing Adjective.
 */
@RestController
@RequestMapping("/api")
public class AdjectiveResource {

    private final Logger log = LoggerFactory.getLogger(AdjectiveResource.class);

    @Inject
    private AdjectiveRepository adjectiveRepository;

    /**
     * POST  /adjectives -> Create a new adjective.
     */
    @RequestMapping(value = "/adjectives",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Adjective adjective) throws URISyntaxException {
        log.debug("REST request to save Adjective : {}", adjective);
        if (adjective.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new adjective cannot already have an ID").build();
        }
        adjectiveRepository.save(adjective);
        return ResponseEntity.created(new URI("/api/adjectives/" + adjective.getId())).build();
    }

    /**
     * PUT  /adjectives -> Updates an existing adjective.
     */
    @RequestMapping(value = "/adjectives",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Adjective adjective) throws URISyntaxException {
        log.debug("REST request to update Adjective : {}", adjective);
        if (adjective.getId() == null) {
            return create(adjective);
        }
        adjectiveRepository.save(adjective);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /adjectives -> get all the adjectives.
     */
    @RequestMapping(value = "/adjectives",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Adjective> getAll() {
        log.debug("REST request to get all Adjectives");
        return adjectiveRepository.findAll();
    }

    /**
     * GET  /adjectives/:id -> get the "id" adjective.
     */
    @RequestMapping(value = "/adjectives/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adjective> get(@PathVariable Long id) {
        log.debug("REST request to get Adjective : {}", id);
        return Optional.ofNullable(adjectiveRepository.findOne(id))
            .map(adjective -> new ResponseEntity<>(
                adjective,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adjectives/:id -> delete the "id" adjective.
     */
    @RequestMapping(value = "/adjectives/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Adjective : {}", id);
        adjectiveRepository.delete(id);
    }
}
