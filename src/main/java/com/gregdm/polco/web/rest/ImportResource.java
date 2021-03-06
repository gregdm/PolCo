package com.gregdm.polco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gregdm.polco.security.AuthoritiesConstants;
import com.gregdm.polco.service.ImportService;
import com.gregdm.polco.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import liquibase.util.csv.opencsv.CSVWriter;

/**
 * REST controller for managing .
 */
@RestController
@RequestMapping("/api/import")
public class ImportResource {

    private final Logger log = LoggerFactory.getLogger(ImportResource.class);

    @Inject
    private TranslationService translationService;
    @Inject
    private ImportService importService;

    @RequestMapping(value = "/CSV",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public String create(@RequestBody MultipartFile file) throws URISyntaxException {
        importService.importCSV(file);
        return "{ \"value\":\"greg\"}";
    }
    @RequestMapping(value = "/exportTrad",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public void exportTrad(HttpServletResponse response) throws URISyntaxException, IOException {

        response.setContentType("text/csv");
        String reportName = "traductions.csv";
        response.setHeader("Content-disposition", "attachment;filename="+reportName);
        PrintWriter out = response.getWriter();
        CSVWriter writer = new CSVWriter(out);
        importService.exportTrad(writer);
        out.flush();
        out.close();
    }

    @RequestMapping(value = "/XML",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public boolean importXML(@RequestBody MultipartFile file) throws URISyntaxException {
        return importService.importXML(file);
    }
}
