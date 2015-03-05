package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.repository.AdverbTransRepository;
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
 * REST controller for managing AdverbTrans.
 */
@RestController
@RequestMapping("/api")
public class AdverbTransResource {

    private final Logger log = LoggerFactory.getLogger(AdverbTransResource.class);

    @Inject
    private AdverbTransRepository adverbTransRepository;

    /**
     * POST  /adverbTranss -> Create a new adverbTrans.
     */
    @RequestMapping(value = "/adverbTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody AdverbTrans adverbTrans) throws URISyntaxException {
        log.debug("REST request to save AdverbTrans : {}", adverbTrans);
        if (adverbTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new adverbTrans cannot already have an ID").build();
        }
        adverbTransRepository.save(adverbTrans);
        return ResponseEntity.created(new URI("/api/adverbTranss/" + adverbTrans.getId())).build();
    }

    /**
     * PUT  /adverbTranss -> Updates an existing adverbTrans.
     */
    @RequestMapping(value = "/adverbTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody AdverbTrans adverbTrans) throws URISyntaxException {
        log.debug("REST request to update AdverbTrans : {}", adverbTrans);
        if (adverbTrans.getId() == null) {
            return create(adverbTrans);
        }
        adverbTransRepository.save(adverbTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /adverbTranss -> get all the adverbTranss.
     */
    @RequestMapping(value = "/adverbTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AdverbTrans> getAll() {
        log.debug("REST request to get all AdverbTranss");
        return adverbTransRepository.findAll();
    }

    /**
     * GET  /adverbTranss/:id -> get the "id" adverbTrans.
     */
    @RequestMapping(value = "/adverbTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdverbTrans> get(@PathVariable Long id) {
        log.debug("REST request to get AdverbTrans : {}", id);
        return Optional.ofNullable(adverbTransRepository.findOne(id))
            .map(adverbTrans -> new ResponseEntity<>(
                adverbTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adverbTranss/:id -> delete the "id" adverbTrans.
     */
    @RequestMapping(value = "/adverbTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete AdverbTrans : {}", id);
        adverbTransRepository.delete(id);
    }
}
