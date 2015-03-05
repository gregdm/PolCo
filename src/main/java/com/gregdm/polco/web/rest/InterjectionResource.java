package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.repository.InterjectionRepository;
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
 * REST controller for managing Interjection.
 */
@RestController
@RequestMapping("/api")
public class InterjectionResource {

    private final Logger log = LoggerFactory.getLogger(InterjectionResource.class);

    @Inject
    private InterjectionRepository interjectionRepository;

    /**
     * POST  /interjections -> Create a new interjection.
     */
    @RequestMapping(value = "/interjections",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Interjection interjection) throws URISyntaxException {
        log.debug("REST request to save Interjection : {}", interjection);
        if (interjection.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new interjection cannot already have an ID").build();
        }
        interjectionRepository.save(interjection);
        return ResponseEntity.created(new URI("/api/interjections/" + interjection.getId())).build();
    }

    /**
     * PUT  /interjections -> Updates an existing interjection.
     */
    @RequestMapping(value = "/interjections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Interjection interjection) throws URISyntaxException {
        log.debug("REST request to update Interjection : {}", interjection);
        if (interjection.getId() == null) {
            return create(interjection);
        }
        interjectionRepository.save(interjection);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /interjections -> get all the interjections.
     */
    @RequestMapping(value = "/interjections",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Interjection> getAll() {
        log.debug("REST request to get all Interjections");
        return interjectionRepository.findAll();
    }

    /**
     * GET  /interjections/:id -> get the "id" interjection.
     */
    @RequestMapping(value = "/interjections/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Interjection> get(@PathVariable Long id) {
        log.debug("REST request to get Interjection : {}", id);
        return Optional.ofNullable(interjectionRepository.findOne(id))
            .map(interjection -> new ResponseEntity<>(
                interjection,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /interjections/:id -> delete the "id" interjection.
     */
    @RequestMapping(value = "/interjections/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Interjection : {}", id);
        interjectionRepository.delete(id);
    }
}
