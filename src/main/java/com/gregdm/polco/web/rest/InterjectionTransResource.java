package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.InterjectionTrans;
import com.gregdm.polco.repository.InterjectionTransRepository;
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
 * REST controller for managing InterjectionTrans.
 */
@RestController
@RequestMapping("/api")
public class InterjectionTransResource {

    private final Logger log = LoggerFactory.getLogger(InterjectionTransResource.class);

    @Inject
    private InterjectionTransRepository interjectionTransRepository;

    /**
     * POST  /interjectionTranss -> Create a new interjectionTrans.
     */
    @RequestMapping(value = "/interjectionTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody InterjectionTrans interjectionTrans) throws URISyntaxException {
        log.debug("REST request to save InterjectionTrans : {}", interjectionTrans);
        if (interjectionTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new interjectionTrans cannot already have an ID").build();
        }
        interjectionTransRepository.save(interjectionTrans);
        return ResponseEntity.created(new URI("/api/interjectionTranss/" + interjectionTrans.getId())).build();
    }

    /**
     * PUT  /interjectionTranss -> Updates an existing interjectionTrans.
     */
    @RequestMapping(value = "/interjectionTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody InterjectionTrans interjectionTrans) throws URISyntaxException {
        log.debug("REST request to update InterjectionTrans : {}", interjectionTrans);
        if (interjectionTrans.getId() == null) {
            return create(interjectionTrans);
        }
        interjectionTransRepository.save(interjectionTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /interjectionTranss -> get all the interjectionTranss.
     */
    @RequestMapping(value = "/interjectionTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InterjectionTrans> getAll() {
        log.debug("REST request to get all InterjectionTranss");
        return interjectionTransRepository.findAll();
    }

    /**
     * GET  /interjectionTranss/:id -> get the "id" interjectionTrans.
     */
    @RequestMapping(value = "/interjectionTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InterjectionTrans> get(@PathVariable Long id) {
        log.debug("REST request to get InterjectionTrans : {}", id);
        return Optional.ofNullable(interjectionTransRepository.findOne(id))
            .map(interjectionTrans -> new ResponseEntity<>(
                interjectionTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /interjectionTranss/:id -> delete the "id" interjectionTrans.
     */
    @RequestMapping(value = "/interjectionTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete InterjectionTrans : {}", id);
        interjectionTransRepository.delete(id);
    }
}
