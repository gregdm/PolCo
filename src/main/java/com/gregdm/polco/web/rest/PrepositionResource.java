package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Preposition;
import com.gregdm.polco.repository.PrepositionRepository;
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
 * REST controller for managing Preposition.
 */
@RestController
@RequestMapping("/api")
public class PrepositionResource {

    private final Logger log = LoggerFactory.getLogger(PrepositionResource.class);

    @Inject
    private PrepositionRepository prepositionRepository;

    /**
     * POST  /prepositions -> Create a new preposition.
     */
    @RequestMapping(value = "/prepositions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Preposition preposition) throws URISyntaxException {
        log.debug("REST request to save Preposition : {}", preposition);
        if (preposition.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new preposition cannot already have an ID").build();
        }
        prepositionRepository.save(preposition);
        return ResponseEntity.created(new URI("/api/prepositions/" + preposition.getId())).build();
    }

    /**
     * PUT  /prepositions -> Updates an existing preposition.
     */
    @RequestMapping(value = "/prepositions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Preposition preposition) throws URISyntaxException {
        log.debug("REST request to update Preposition : {}", preposition);
        if (preposition.getId() == null) {
            return create(preposition);
        }
        prepositionRepository.save(preposition);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /prepositions -> get all the prepositions.
     */
    @RequestMapping(value = "/prepositions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Preposition> getAll() {
        log.debug("REST request to get all Prepositions");
        return prepositionRepository.findAll();
    }

    /**
     * GET  /prepositions/:id -> get the "id" preposition.
     */
    @RequestMapping(value = "/prepositions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preposition> get(@PathVariable Long id) {
        log.debug("REST request to get Preposition : {}", id);
        return Optional.ofNullable(prepositionRepository.findOne(id))
            .map(preposition -> new ResponseEntity<>(
                preposition,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prepositions/:id -> delete the "id" preposition.
     */
    @RequestMapping(value = "/prepositions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Preposition : {}", id);
        prepositionRepository.delete(id);
    }
}
