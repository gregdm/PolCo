package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.NominalDet;
import com.gregdm.polco.repository.NominalDetRepository;
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
 * REST controller for managing NominalDet.
 */
@RestController
@RequestMapping("/api")
public class NominalDetResource {

    private final Logger log = LoggerFactory.getLogger(NominalDetResource.class);

    @Inject
    private NominalDetRepository nominalDetRepository;

    /**
     * POST  /nominalDets -> Create a new nominalDet.
     */
    @RequestMapping(value = "/nominalDets",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody NominalDet nominalDet) throws URISyntaxException {
        log.debug("REST request to save NominalDet : {}", nominalDet);
        if (nominalDet.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new nominalDet cannot already have an ID").build();
        }
        nominalDetRepository.save(nominalDet);
        return ResponseEntity.created(new URI("/api/nominalDets/" + nominalDet.getId())).build();
    }

    /**
     * PUT  /nominalDets -> Updates an existing nominalDet.
     */
    @RequestMapping(value = "/nominalDets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody NominalDet nominalDet) throws URISyntaxException {
        log.debug("REST request to update NominalDet : {}", nominalDet);
        if (nominalDet.getId() == null) {
            return create(nominalDet);
        }
        nominalDetRepository.save(nominalDet);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /nominalDets -> get all the nominalDets.
     */
    @RequestMapping(value = "/nominalDets",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NominalDet> getAll() {
        log.debug("REST request to get all NominalDets");
        return nominalDetRepository.findAll();
    }

    /**
     * GET  /nominalDets/:id -> get the "id" nominalDet.
     */
    @RequestMapping(value = "/nominalDets/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NominalDet> get(@PathVariable Long id) {
        log.debug("REST request to get NominalDet : {}", id);
        return Optional.ofNullable(nominalDetRepository.findOne(id))
            .map(nominalDet -> new ResponseEntity<>(
                nominalDet,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /nominalDets/:id -> delete the "id" nominalDet.
     */
    @RequestMapping(value = "/nominalDets/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete NominalDet : {}", id);
        nominalDetRepository.delete(id);
    }
}
