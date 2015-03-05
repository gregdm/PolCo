package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.PrepositionTrans;
import com.gregdm.polco.repository.PrepositionTransRepository;
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
 * REST controller for managing PrepositionTrans.
 */
@RestController
@RequestMapping("/api")
public class PrepositionTransResource {

    private final Logger log = LoggerFactory.getLogger(PrepositionTransResource.class);

    @Inject
    private PrepositionTransRepository prepositionTransRepository;

    /**
     * POST  /prepositionTranss -> Create a new prepositionTrans.
     */
    @RequestMapping(value = "/prepositionTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody PrepositionTrans prepositionTrans) throws URISyntaxException {
        log.debug("REST request to save PrepositionTrans : {}", prepositionTrans);
        if (prepositionTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prepositionTrans cannot already have an ID").build();
        }
        prepositionTransRepository.save(prepositionTrans);
        return ResponseEntity.created(new URI("/api/prepositionTranss/" + prepositionTrans.getId())).build();
    }

    /**
     * PUT  /prepositionTranss -> Updates an existing prepositionTrans.
     */
    @RequestMapping(value = "/prepositionTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody PrepositionTrans prepositionTrans) throws URISyntaxException {
        log.debug("REST request to update PrepositionTrans : {}", prepositionTrans);
        if (prepositionTrans.getId() == null) {
            return create(prepositionTrans);
        }
        prepositionTransRepository.save(prepositionTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /prepositionTranss -> get all the prepositionTranss.
     */
    @RequestMapping(value = "/prepositionTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrepositionTrans> getAll() {
        log.debug("REST request to get all PrepositionTranss");
        return prepositionTransRepository.findAll();
    }

    /**
     * GET  /prepositionTranss/:id -> get the "id" prepositionTrans.
     */
    @RequestMapping(value = "/prepositionTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrepositionTrans> get(@PathVariable Long id) {
        log.debug("REST request to get PrepositionTrans : {}", id);
        return Optional.ofNullable(prepositionTransRepository.findOne(id))
            .map(prepositionTrans -> new ResponseEntity<>(
                prepositionTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prepositionTranss/:id -> delete the "id" prepositionTrans.
     */
    @RequestMapping(value = "/prepositionTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete PrepositionTrans : {}", id);
        prepositionTransRepository.delete(id);
    }
}
