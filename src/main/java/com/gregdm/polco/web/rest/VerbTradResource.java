package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.VerbTrad;
import com.gregdm.polco.repository.VerbTradRepository;
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
 * REST controller for managing VerbTrad.
 */
@RestController
@RequestMapping("/api")
public class VerbTradResource {

    private final Logger log = LoggerFactory.getLogger(VerbTradResource.class);

    @Inject
    private VerbTradRepository verbTradRepository;

    /**
     * POST  /verbTrads -> Create a new verbTrad.
     */
    @RequestMapping(value = "/verbTrads",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody VerbTrad verbTrad) throws URISyntaxException {
        log.debug("REST request to save VerbTrad : {}", verbTrad);
        if (verbTrad.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new verbTrad cannot already have an ID").build();
        }
        verbTradRepository.save(verbTrad);
        return ResponseEntity.created(new URI("/api/verbTrads/" + verbTrad.getId())).build();
    }

    /**
     * PUT  /verbTrads -> Updates an existing verbTrad.
     */
    @RequestMapping(value = "/verbTrads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody VerbTrad verbTrad) throws URISyntaxException {
        log.debug("REST request to update VerbTrad : {}", verbTrad);
        if (verbTrad.getId() == null) {
            return create(verbTrad);
        }
        verbTradRepository.save(verbTrad);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /verbTrads -> get all the verbTrads.
     */
    @RequestMapping(value = "/verbTrads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VerbTrad> getAll() {
        log.debug("REST request to get all VerbTrads");
        return verbTradRepository.findAll();
    }

    /**
     * GET  /verbTrads/:id -> get the "id" verbTrad.
     */
    @RequestMapping(value = "/verbTrads/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VerbTrad> get(@PathVariable Long id) {
        log.debug("REST request to get VerbTrad : {}", id);
        return Optional.ofNullable(verbTradRepository.findOne(id))
            .map(verbTrad -> new ResponseEntity<>(
                verbTrad,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /verbTrads/:id -> delete the "id" verbTrad.
     */
    @RequestMapping(value = "/verbTrads/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete VerbTrad : {}", id);
        verbTradRepository.delete(id);
    }
}
