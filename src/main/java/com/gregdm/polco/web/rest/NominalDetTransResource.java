package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.NominalDetTrans;
import com.gregdm.polco.repository.NominalDetTransRepository;
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
 * REST controller for managing NominalDetTrans.
 */
@RestController
@RequestMapping("/api")
public class NominalDetTransResource {

    private final Logger log = LoggerFactory.getLogger(NominalDetTransResource.class);

    @Inject
    private NominalDetTransRepository nominalDetTransRepository;

    /**
     * POST  /nominalDetTranss -> Create a new nominalDetTrans.
     */
    @RequestMapping(value = "/nominalDetTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody NominalDetTrans nominalDetTrans) throws URISyntaxException {
        log.debug("REST request to save NominalDetTrans : {}", nominalDetTrans);
        if (nominalDetTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new nominalDetTrans cannot already have an ID").build();
        }
        nominalDetTransRepository.save(nominalDetTrans);
        return ResponseEntity.created(new URI("/api/nominalDetTranss/" + nominalDetTrans.getId())).build();
    }

    /**
     * PUT  /nominalDetTranss -> Updates an existing nominalDetTrans.
     */
    @RequestMapping(value = "/nominalDetTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody NominalDetTrans nominalDetTrans) throws URISyntaxException {
        log.debug("REST request to update NominalDetTrans : {}", nominalDetTrans);
        if (nominalDetTrans.getId() == null) {
            return create(nominalDetTrans);
        }
        nominalDetTransRepository.save(nominalDetTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /nominalDetTranss -> get all the nominalDetTranss.
     */
    @RequestMapping(value = "/nominalDetTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NominalDetTrans> getAll() {
        log.debug("REST request to get all NominalDetTranss");
        return nominalDetTransRepository.findAll();
    }

    /**
     * GET  /nominalDetTranss/:id -> get the "id" nominalDetTrans.
     */
    @RequestMapping(value = "/nominalDetTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NominalDetTrans> get(@PathVariable Long id) {
        log.debug("REST request to get NominalDetTrans : {}", id);
        return Optional.ofNullable(nominalDetTransRepository.findOne(id))
            .map(nominalDetTrans -> new ResponseEntity<>(
                nominalDetTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /nominalDetTranss/:id -> delete the "id" nominalDetTrans.
     */
    @RequestMapping(value = "/nominalDetTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete NominalDetTrans : {}", id);
        nominalDetTransRepository.delete(id);
    }
}
