package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.service.PourcentageService;
import com.gregdm.polco.service.TranslationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.URISyntaxException;

/**
 * REST controller for managing BadWord.
 */
@RestController
@RequestMapping("/api")
public class PourcentageResource {

    private final Logger log = LoggerFactory.getLogger(PourcentageResource.class);

    @Inject
    private PourcentageService pourcentageService;

    /**
     * POST  /badWords -> Create a new
     */
    @RequestMapping(value = "/pourcentage",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<String> getPourcentage(@RequestBody String text) throws URISyntaxException {
        String translatedText = pourcentageService.getPourcentage(text);
        if(StringUtils.isNoneEmpty(translatedText)){
            return new ResponseEntity<>(translatedText,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
