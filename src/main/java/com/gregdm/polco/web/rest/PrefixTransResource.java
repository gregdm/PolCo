package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.PrefixTrans;
import com.gregdm.polco.repository.PrefixTransRepository;
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
 * REST controller for managing PrefixTrans.
 */
@RestController
@RequestMapping("/api")
public class PrefixTransResource {

    private final Logger log = LoggerFactory.getLogger(PrefixTransResource.class);

    @Inject
    private PrefixTransRepository prefixTransRepository;

    /**
     * POST  /prefixTranss -> Create a new prefixTrans.
     */
    @RequestMapping(value = "/prefixTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody PrefixTrans prefixTrans) throws URISyntaxException {
        log.debug("REST request to save PrefixTrans : {}", prefixTrans);
        if (prefixTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prefixTrans cannot already have an ID").build();
        }
        prefixTransRepository.save(prefixTrans);
        return ResponseEntity.created(new URI("/api/prefixTranss/" + prefixTrans.getId())).build();
    }

    /**
     * PUT  /prefixTranss -> Updates an existing prefixTrans.
     */
    @RequestMapping(value = "/prefixTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody PrefixTrans prefixTrans) throws URISyntaxException {
        log.debug("REST request to update PrefixTrans : {}", prefixTrans);
        if (prefixTrans.getId() == null) {
            return create(prefixTrans);
        }
        prefixTransRepository.save(prefixTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /prefixTranss -> get all the prefixTranss.
     */
    @RequestMapping(value = "/prefixTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrefixTrans> getAll() {
        log.debug("REST request to get all PrefixTranss");
        return prefixTransRepository.findAll();
    }

    /**
     * GET  /prefixTranss/:id -> get the "id" prefixTrans.
     */
    @RequestMapping(value = "/prefixTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrefixTrans> get(@PathVariable Long id) {
        log.debug("REST request to get PrefixTrans : {}", id);
        return Optional.ofNullable(prefixTransRepository.findOne(id))
            .map(prefixTrans -> new ResponseEntity<>(
                prefixTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prefixTranss/:id -> delete the "id" prefixTrans.
     */
    @RequestMapping(value = "/prefixTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PrefixTrans : {}", id);
        prefixTransRepository.delete(id);
    }
}
