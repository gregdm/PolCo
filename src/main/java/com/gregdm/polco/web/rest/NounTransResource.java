package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.repository.NounTransRepository;
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
 * REST controller for managing NounTrans.
 */
@RestController
@RequestMapping("/api")
public class NounTransResource {

    private final Logger log = LoggerFactory.getLogger(NounTransResource.class);

    @Inject
    private NounTransRepository nounTransRepository;

    /**
     * POST  /nounTranss -> Create a new nounTrans.
     */
    @RequestMapping(value = "/nounTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody NounTrans nounTrans) throws URISyntaxException {
        log.debug("REST request to save NounTrans : {}", nounTrans);
        if (nounTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new nounTrans cannot already have an ID").build();
        }
        nounTransRepository.save(nounTrans);
        return ResponseEntity.created(new URI("/api/nounTranss/" + nounTrans.getId())).build();
    }

    /**
     * PUT  /nounTranss -> Updates an existing nounTrans.
     */
    @RequestMapping(value = "/nounTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody NounTrans nounTrans) throws URISyntaxException {
        log.debug("REST request to update NounTrans : {}", nounTrans);
        if (nounTrans.getId() == null) {
            return create(nounTrans);
        }
        nounTransRepository.save(nounTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /nounTranss -> get all the nounTranss.
     */
    @RequestMapping(value = "/nounTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NounTrans> getAll() {
        log.debug("REST request to get all NounTranss");
        return nounTransRepository.findAll();
    }

    /**
     * GET  /nounTranss/:id -> get the "id" nounTrans.
     */
    @RequestMapping(value = "/nounTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NounTrans> get(@PathVariable Long id) {
        log.debug("REST request to get NounTrans : {}", id);
        return Optional.ofNullable(nounTransRepository.findOne(id))
            .map(nounTrans -> new ResponseEntity<>(
                nounTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /nounTranss/:id -> delete the "id" nounTrans.
     */
    @RequestMapping(value = "/nounTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete NounTrans : {}", id);
        nounTransRepository.delete(id);
    }
}
