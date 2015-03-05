package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.VerbTrans;
import com.gregdm.polco.repository.VerbTransRepository;
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
 * REST controller for managing VerbTrans.
 */
@RestController
@RequestMapping("/api")
public class VerbTransResource {

    private final Logger log = LoggerFactory.getLogger(VerbTransResource.class);

    @Inject
    private VerbTransRepository verbTransRepository;

    /**
     * POST  /verbTranss -> Create a new verbTrans.
     */
    @RequestMapping(value = "/verbTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody VerbTrans verbTrans) throws URISyntaxException {
        log.debug("REST request to save VerbTrans : {}", verbTrans);
        if (verbTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new verbTrans cannot already have an ID").build();
        }
        verbTransRepository.save(verbTrans);
        return ResponseEntity.created(new URI("/api/verbTranss/" + verbTrans.getId())).build();
    }

    /**
     * PUT  /verbTranss -> Updates an existing verbTrans.
     */
    @RequestMapping(value = "/verbTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody VerbTrans verbTrans) throws URISyntaxException {
        log.debug("REST request to update VerbTrans : {}", verbTrans);
        if (verbTrans.getId() == null) {
            return create(verbTrans);
        }
        verbTransRepository.save(verbTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /verbTranss -> get all the verbTranss.
     */
    @RequestMapping(value = "/verbTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VerbTrans> getAll() {
        log.debug("REST request to get all VerbTranss");
        return verbTransRepository.findAll();
    }

    /**
     * GET  /verbTranss/:id -> get the "id" verbTrans.
     */
    @RequestMapping(value = "/verbTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VerbTrans> get(@PathVariable Long id) {
        log.debug("REST request to get VerbTrans : {}", id);
        return Optional.ofNullable(verbTransRepository.findOne(id))
            .map(verbTrans -> new ResponseEntity<>(
                verbTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /verbTranss/:id -> delete the "id" verbTrans.
     */
    @RequestMapping(value = "/verbTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete VerbTrans : {}", id);
        verbTransRepository.delete(id);
    }
}
