package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.GoodWord;
import com.gregdm.polco.repository.GoodWordRepository;
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
 * REST controller for managing GoodWord.
 */
@RestController
@RequestMapping("/api")
public class GoodWordResource {

    private final Logger log = LoggerFactory.getLogger(GoodWordResource.class);

    @Inject
    private GoodWordRepository goodWordRepository;

    /**
     * POST  /goodWords -> Create a new goodWord.
     */
    @RequestMapping(value = "/goodWords",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody GoodWord goodWord) throws URISyntaxException {
        log.debug("REST request to save GoodWord : {}", goodWord);
        if (goodWord.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new goodWord cannot already have an ID").build();
        }
        goodWordRepository.save(goodWord);
        return ResponseEntity.created(new URI("/api/goodWords/" + goodWord.getId())).build();
    }

    /**
     * PUT  /goodWords -> Updates an existing goodWord.
     */
    @RequestMapping(value = "/goodWords",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody GoodWord goodWord) throws URISyntaxException {
        log.debug("REST request to update GoodWord : {}", goodWord);
        if (goodWord.getId() == null) {
            return create(goodWord);
        }
        goodWordRepository.save(goodWord);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /goodWords -> get all the goodWords.
     */
    @RequestMapping(value = "/goodWords",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GoodWord> getAll() {
        log.debug("REST request to get all GoodWords");
        return goodWordRepository.findAll();
    }

    /**
     * GET  /goodWords/:id -> get the "id" goodWord.
     */
    @RequestMapping(value = "/goodWords/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GoodWord> get(@PathVariable Long id) {
        log.debug("REST request to get GoodWord : {}", id);
        return Optional.ofNullable(goodWordRepository.findOne(id))
            .map(goodWord -> new ResponseEntity<>(
                goodWord,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /goodWords/:id -> delete the "id" goodWord.
     */
    @RequestMapping(value = "/goodWords/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete GoodWord : {}", id);
        goodWordRepository.delete(id);
    }
}
