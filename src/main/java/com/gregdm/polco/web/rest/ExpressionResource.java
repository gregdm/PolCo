package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.repository.ExpressionRepository;
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
 * REST controller for managing Expression.
 */
@RestController
@RequestMapping("/api")
public class ExpressionResource {

    private final Logger log = LoggerFactory.getLogger(ExpressionResource.class);

    @Inject
    private ExpressionRepository expressionRepository;

    /**
     * POST  /expressions -> Create a new expression.
     */
    @RequestMapping(value = "/expressions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Expression expression) throws URISyntaxException {
        log.debug("REST request to save Expression : {}", expression);
        if (expression.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new expression cannot already have an ID").build();
        }
        expressionRepository.save(expression);
        return ResponseEntity.created(new URI("/api/expressions/" + expression.getId())).build();
    }

    /**
     * PUT  /expressions -> Updates an existing expression.
     */
    @RequestMapping(value = "/expressions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Expression expression) throws URISyntaxException {
        log.debug("REST request to update Expression : {}", expression);
        if (expression.getId() == null) {
            return create(expression);
        }
        expressionRepository.save(expression);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /expressions -> get all the expressions.
     */
    @RequestMapping(value = "/expressions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Expression> getAll() {
        log.debug("REST request to get all Expressions");
        return expressionRepository.findAll();
    }

    /**
     * GET  /expressions/:id -> get the "id" expression.
     */
    @RequestMapping(value = "/expressions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expression> get(@PathVariable Long id) {
        log.debug("REST request to get Expression : {}", id);
        return Optional.ofNullable(expressionRepository.findOne(id))
            .map(expression -> new ResponseEntity<>(
                expression,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expressions/:id -> delete the "id" expression.
     */
    @RequestMapping(value = "/expressions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Expression : {}", id);
        expressionRepository.delete(id);
    }
}
