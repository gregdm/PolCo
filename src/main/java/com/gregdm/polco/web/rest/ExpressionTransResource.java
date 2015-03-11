package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.ExpressionTrans;
import com.gregdm.polco.repository.ExpressionTransRepository;
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
 * REST controller for managing ExpressionTrans.
 */
@RestController
@RequestMapping("/api")
public class ExpressionTransResource {

    private final Logger log = LoggerFactory.getLogger(ExpressionTransResource.class);

    @Inject
    private ExpressionTransRepository expressionTransRepository;

    /**
     * POST  /expressionTranss -> Create a new expressionTrans.
     */
    @RequestMapping(value = "/expressionTranss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody ExpressionTrans expressionTrans) throws URISyntaxException {
        log.debug("REST request to save ExpressionTrans : {}", expressionTrans);
        if (expressionTrans.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new expressionTrans cannot already have an ID").build();
        }
        expressionTransRepository.save(expressionTrans);
        return ResponseEntity.created(new URI("/api/expressionTranss/" + expressionTrans.getId())).build();
    }

    /**
     * PUT  /expressionTranss -> Updates an existing expressionTrans.
     */
    @RequestMapping(value = "/expressionTranss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody ExpressionTrans expressionTrans) throws URISyntaxException {
        log.debug("REST request to update ExpressionTrans : {}", expressionTrans);
        if (expressionTrans.getId() == null) {
            return create(expressionTrans);
        }
        expressionTransRepository.save(expressionTrans);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /expressionTranss -> get all the expressionTranss.
     */
    @RequestMapping(value = "/expressionTranss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExpressionTrans> getAll() {
        log.debug("REST request to get all ExpressionTranss");
        return expressionTransRepository.findAll();
    }

    /**
     * GET  /expressionTranss/:id -> get the "id" expressionTrans.
     */
    @RequestMapping(value = "/expressionTranss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpressionTrans> get(@PathVariable Long id) {
        log.debug("REST request to get ExpressionTrans : {}", id);
        return Optional.ofNullable(expressionTransRepository.findOne(id))
            .map(expressionTrans -> new ResponseEntity<>(
                expressionTrans,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expressionTranss/:id -> delete the "id" expressionTrans.
     */
    @RequestMapping(value = "/expressionTranss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ExpressionTrans : {}", id);
        expressionTransRepository.delete(id);
    }
}
