package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.security.AuthoritiesConstants;
import com.gregdm.polco.service.TranslationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * REST controller for managing BadWord.
 */
@RestController
@RequestMapping("/api")
public class TranslationResource {

    private final Logger log = LoggerFactory.getLogger(TranslationResource.class);

    @Inject
    private TranslationService translationService;

    /**
     * POST  /badWords -> Create a new
     */
    @RequestMapping(value = "/translation",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<String> translate(@RequestBody String text) throws URISyntaxException {
        String translatedText = translationService.translateText(text);
        if(StringUtils.isNoneEmpty(translatedText)){
            return new ResponseEntity<>(translatedText,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
