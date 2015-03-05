package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.repository.NounRepository;
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
 * REST controller for managing Noun.
 */
@RestController
@RequestMapping("/api")
public class NounResource {

    private final Logger log = LoggerFactory.getLogger(NounResource.class);

    @Inject
    private NounRepository nounRepository;

    /**
     * POST  /nouns -> Create a new noun.
     */
    @RequestMapping(value = "/nouns",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Noun noun) throws URISyntaxException {
        log.debug("REST request to save Noun : {}", noun);
        if (noun.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new noun cannot already have an ID").build();
        }
        nounRepository.save(noun);
        return ResponseEntity.created(new URI("/api/nouns/" + noun.getId())).build();
    }

    /**
     * PUT  /nouns -> Updates an existing noun.
     */
    @RequestMapping(value = "/nouns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Noun noun) throws URISyntaxException {
        log.debug("REST request to update Noun : {}", noun);
        if (noun.getId() == null) {
            return create(noun);
        }
        nounRepository.save(noun);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /nouns -> get all the nouns.
     */
    @RequestMapping(value = "/nouns",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Noun> getAll() {
        log.debug("REST request to get all Nouns");
        return nounRepository.findAll();
    }

    /**
     * GET  /nouns/:id -> get the "id" noun.
     */
    @RequestMapping(value = "/nouns/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Noun> get(@PathVariable Long id) {
        log.debug("REST request to get Noun : {}", id);
        return Optional.ofNullable(nounRepository.findOne(id))
            .map(noun -> new ResponseEntity<>(
                noun,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /nouns/:id -> delete the "id" noun.
     */
    @RequestMapping(value = "/nouns/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Noun : {}", id);
        nounRepository.delete(id);
    }
}
