package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.domain.BadWord;
import com.gregdm.polco.repository.BadWordRepository;
import com.gregdm.polco.service.TranslationService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BadWord.
 */
@RestController
@RequestMapping("/api")
public class TranslationResource {

    private final Logger log = LoggerFactory.getLogger(TranslationResource.class);

    @Inject
    private BadWordRepository badWordRepository;

    @Inject
    private TranslationService translationService;

    /**
     * POST  /badWords -> Create a new badWord.
     */
    @RequestMapping(value = "/translation",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String create(@RequestBody String text) throws URISyntaxException {

        String translatedText = translationService.translateText(text);
        return "{ \"value\":\""+ translatedText +"\"}";
    }

    @RequestMapping(value = "/translation/import",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String create(@RequestBody MultipartFile file) throws URISyntaxException {
        translationService.importCSV(file);
        return "{ \"value\":\"greg\"}";
    }

    @RequestMapping(value = "/translation/importXML",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String importXML(@RequestBody MultipartFile file) throws URISyntaxException {
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-c-d.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-d-e.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-d-e-2.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-e-f.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-f-l.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-l-p.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-o-p.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-p-r.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-r-t.xml");
        translationService.importXML("C:\\Users\\Greg\\Desktop\\Dictio\\dico-t-z.xml");
        return "{ \"value\":\"greg\"}";
    }
}
