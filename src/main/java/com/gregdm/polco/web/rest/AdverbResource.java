package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.repository.AdverbRepository;
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
 * REST controller for managing Adverb.
 */
@RestController
@RequestMapping("/api")
public class AdverbResource {

    private final Logger log = LoggerFactory.getLogger(AdverbResource.class);

    @Inject
    private AdverbRepository adverbRepository;

    /**
     * POST  /adverbs -> Create a new adverb.
     */
    @RequestMapping(value = "/adverbs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Adverb adverb) throws URISyntaxException {
        log.debug("REST request to save Adverb : {}", adverb);
        if (adverb.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new adverb cannot already have an ID").build();
        }
        adverbRepository.save(adverb);
        return ResponseEntity.created(new URI("/api/adverbs/" + adverb.getId())).build();
    }

    /**
     * PUT  /adverbs -> Updates an existing adverb.
     */
    @RequestMapping(value = "/adverbs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Adverb adverb) throws URISyntaxException {
        log.debug("REST request to update Adverb : {}", adverb);
        if (adverb.getId() == null) {
            return create(adverb);
        }
        adverbRepository.save(adverb);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /adverbs -> get all the adverbs.
     */
    @RequestMapping(value = "/adverbs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Adverb> getAll() {
        log.debug("REST request to get all Adverbs");
        return adverbRepository.findAll();
    }

    /**
     * GET  /adverbs/:id -> get the "id" adverb.
     */
    @RequestMapping(value = "/adverbs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Adverb> get(@PathVariable Long id) {
        log.debug("REST request to get Adverb : {}", id);
        return Optional.ofNullable(adverbRepository.findOne(id))
            .map(adverb -> new ResponseEntity<>(
                adverb,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /adverbs/:id -> delete the "id" adverb.
     */
    @RequestMapping(value = "/adverbs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Adverb : {}", id);
        adverbRepository.delete(id);
    }
}
