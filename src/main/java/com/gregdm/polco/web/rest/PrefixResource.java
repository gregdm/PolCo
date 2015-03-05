package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Prefix;
import com.gregdm.polco.repository.PrefixRepository;
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
 * REST controller for managing Prefix.
 */
@RestController
@RequestMapping("/api")
public class PrefixResource {

    private final Logger log = LoggerFactory.getLogger(PrefixResource.class);

    @Inject
    private PrefixRepository prefixRepository;

    /**
     * POST  /prefixs -> Create a new prefix.
     */
    @RequestMapping(value = "/prefixs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Prefix prefix) throws URISyntaxException {
        log.debug("REST request to save Prefix : {}", prefix);
        if (prefix.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prefix cannot already have an ID").build();
        }
        prefixRepository.save(prefix);
        return ResponseEntity.created(new URI("/api/prefixs/" + prefix.getId())).build();
    }

    /**
     * PUT  /prefixs -> Updates an existing prefix.
     */
    @RequestMapping(value = "/prefixs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Prefix prefix) throws URISyntaxException {
        log.debug("REST request to update Prefix : {}", prefix);
        if (prefix.getId() == null) {
            return create(prefix);
        }
        prefixRepository.save(prefix);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /prefixs -> get all the prefixs.
     */
    @RequestMapping(value = "/prefixs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Prefix> getAll() {
        log.debug("REST request to get all Prefixs");
        return prefixRepository.findAll();
    }

    /**
     * GET  /prefixs/:id -> get the "id" prefix.
     */
    @RequestMapping(value = "/prefixs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Prefix> get(@PathVariable Long id) {
        log.debug("REST request to get Prefix : {}", id);
        return Optional.ofNullable(prefixRepository.findOne(id))
            .map(prefix -> new ResponseEntity<>(
                prefix,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prefixs/:id -> delete the "id" prefix.
     */
    @RequestMapping(value = "/prefixs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Prefix : {}", id);
        prefixRepository.delete(id);
    }
}
