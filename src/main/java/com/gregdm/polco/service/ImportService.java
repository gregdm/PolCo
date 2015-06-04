package com.gregdm.polco.service;

import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.domain.ExpressionTrans;
import com.gregdm.polco.domain.InterjectionTrans;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.domain.VerbTrans;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.service.ImportXML.DicoSAXParser;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.BatchSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import liquibase.util.csv.CSVReader;
import liquibase.util.csv.opencsv.CSVWriter;

@Service
@Transactional
public class ImportService {

    private final Logger log = LoggerFactory.getLogger(ImportService.class);

    @PersistenceContext
    private EntityManager entityManager;

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
    private ExpressionService expressionService;
    @Inject
    private WordValidationService wordValidationService;

    public CSVWriter exportTrad(CSVWriter writer) throws IOException {

        List<WordValidation> words = new ArrayList<>();

        List<ExpressionTrans> expressionTrans = expressionService.findAllTrans();
        expressionTrans.forEach(e -> words.add(wordValidationService.expressionTransToWordValidation(e)));

        List<NounTrans> allNounTrans = nounService.findAllNounTrans();
        allNounTrans.forEach(n -> words.add(wordValidationService.nounTransToWordValidation(n)));

        List<AdjectiveTrans> allAdjectiveTrans = adjectiveService.findAllAdjectiveTrans();
        allAdjectiveTrans.forEach(n -> words.add(wordValidationService.adjectiveTransToWordValidation(
            n)));

        List<VerbTrans> allVerbTrans = verbService.findAllVerbTrans();
        allVerbTrans.forEach(n -> words.add(wordValidationService.verbTransToWordValidation(n)));

        List<InterjectionTrans> allInterjectionTrans = interjectionService.findAllInterjectionTrans();
        allInterjectionTrans.forEach(n -> words.add(wordValidationService.interjectionTransToWordValidation(
            n)));

        List<AdverbTrans> allAdverbTrans = adverbService.findAllAdverbTrans();
        allAdverbTrans.forEach(n -> words.add(wordValidationService.adverbTransToWordValidation(n)));

        String[] header = {"Value","Traduction","WordType","Number","Gender","Person","Tense"};
        writer.writeNext(header);
        for(WordValidation word : words){
            String[] row = {word.getValue(),word.getTranslation(),word.getWordType(),word.getNumber(),word.getGender(),word.getPerson(),word.getTense()};
            writer.writeNext(row);
        }
        return writer;
    }

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


    public boolean importXML(MultipartFile file) {

        Session session = getCurrentSession();
        //TODO GREG A ameliorer faire un batch avec des commit flush par paquet dans hibernate
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DicoSAXParser dicoSAXParser = new DicoSAXParser();

            saxParser.parse(file.getInputStream(), dicoSAXParser);

            AtomicInteger index = new AtomicInteger();
            Transaction tx = session.beginTransaction();
            dicoSAXParser.getVerbList().forEach(n -> {verbService.findOrCreate(n); if(index.get() % 250 == 0){ session.flush();session.clear();}  index.incrementAndGet();});
            tx.commit();
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

    protected Session getCurrentSession()  {
        return entityManager.unwrap(Session.class);
    }

}
