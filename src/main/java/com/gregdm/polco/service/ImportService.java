package com.gregdm.polco.service;

import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.service.ImportXML.DicoSAXParser;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import liquibase.util.csv.CSVReader;

@Service
@Transactional
public class ImportService {

    private final Logger log = LoggerFactory.getLogger(ImportService.class);

    @Inject
    private NounService nounService;

    @Inject
    private AdjectiveService adjectiveService;
    @Inject
    private VerbService verbService;
    @Inject
    private PrefixService prefixService;
    @Inject
    private PrepositionService prepositionService;
    @Inject
    private NominalDetService nominalDetService;
    @Inject
    private InterjectionService interjectionService;
    @Inject
    private AdverbService adverbService;

    @Inject
    private WordValidationService wordValidationService;

    public void importCSV(MultipartFile file) {
        if (!file.isEmpty()) {
            List<WordValidation> words = new ArrayList<>();
            try {

                CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
                String[] row = null;
                while ((row = reader.readNext()) != null) {
                    if (row.length > 1) {
                        WordValidation word = new WordValidation();
                        if (StringUtils.isBlank(row[0]) || StringUtils.isBlank(row[1])
                            || StringUtils.isBlank(row[2])) {
                            break;
                        }
                        word.setValue(row[0]);
                        word.setTranslation(row[1]);
                        word.setWordType(row[2]);
                        word.setNumber(row[3]);
                        word.setGender(row[4]);
                        word.setPerson(row[5]);
                        word.setTense(row[6]);
                        words.add(word);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            words.forEach(w -> wordValidationService.validate(w));
        }
    }

    //TODO GREG
    public void exportTraductionCSV() {
        ///CSVWriter write = new CSVWriter();


    }

    public boolean importXML(MultipartFile file) {
        //TODO GREG A ameliorer faire un batch avec des commit flush par paquet dans hibernate

        //TODO GREG Arriver a récupere les nouveaux mots en temps réel du parser XML (peut être pas)
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DicoSAXParser dicoSAXParser = new DicoSAXParser();

            //saxParser.parse(file.getInputStream(), dicoSAXParser);
            saxParser.parse(file.getInputStream(), dicoSAXParser);

            dicoSAXParser.getVerbList().forEach(n -> verbService.findOrCreate(n));
            dicoSAXParser.getAdverbList().forEach(n -> adverbService.findOrCreate(n));
            dicoSAXParser.getInterjectionList().forEach(n -> interjectionService.findOrCreate(n));
            dicoSAXParser.getNominalDetList().forEach(n -> nominalDetService.findOrCreate(n));
            dicoSAXParser.getPrefixList().forEach(n -> prefixService.findOrCreate(n));
            dicoSAXParser.getPrepositionList().forEach(n -> prepositionService.findOrCreate(n));
            dicoSAXParser.getNounList().forEach(n -> nounService.findOrCreate(n));
            dicoSAXParser.getAdjectiveList().forEach(n -> adjectiveService.findOrCreate(n));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public void importExpression(MultipartFile file) {

    }
}
