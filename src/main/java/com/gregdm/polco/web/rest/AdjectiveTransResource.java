package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.repository.AdjectiveTransRepository;
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
 * REST controller for managing AdjectiveTrans.
 */
@RestController
@RequestMapping("/api")
public class AdjectiveTransResource {

    private final Logger log = LoggerFactory.getLogger(AdjectiveTransResource.class);

    @Inject
    private AdjectiveTransRepository adjectiveTransRepository;

    /**
     * POST  /adjectiveTranss -> Create a new adjectiveTrans.
     */
    @RequestMapping(value = "/adjectiveTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody AdjectiveTrans adjectiveTrans) throws URISyntaxException {
        log.debug("REST request to save AdjectiveTrans : {}", adjectiveTrans);
        if (adjectiveTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new adjectiveTrans cannot already have an ID").build();
        }
        adjectiveTransRepository.save(adjectiveTrans);
        return ResponseEntity.created(new URI("/api/adjectiveTranss/" + adjectiveTrans.getId())).build();
    }

    /**
     * PUT  /adjectiveTranss -> Updates an existing adjectiveTrans.
     */
    @RequestMapping(value = "/adjectiveTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody AdjectiveTrans adjectiveTrans) throws URISyntaxException {
        log.debug("REST request to update AdjectiveTrans : {}", adjectiveTrans);
        if (adjectiveTrans.getId() == null) {
            return create(adjectiveTrans);
        }
        adjectiveTransRepository.save(adjectiveTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /adjectiveTranss -> get all the adjectiveTranss.
     */
    @RequestMapping(value = "/adjectiveTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AdjectiveTrans> getAll() {
        log.debug("REST request to get all AdjectiveTranss");
        return adjectiveTransRepository.findAll();
    }

    /**
     * GET  /adjectiveTranss/:id -> get the "id" adjectiveTrans.
     */
    @RequestMapping(value = "/adjectiveTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AdjectiveTrans> get(@PathVariable Long id) {
        log.debug("REST request to get AdjectiveTrans : {}", id);
        return Optional.ofNullable(adjectiveTransRepository.findOne(id))
            .map(adjectiveTrans -> new ResponseEntity<>(
                adjectiveTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adjectiveTranss/:id -> delete the "id" adjectiveTrans.
     */
    @RequestMapping(value = "/adjectiveTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete AdjectiveTrans : {}", id);
        adjectiveTransRepository.delete(id);
    }
}
