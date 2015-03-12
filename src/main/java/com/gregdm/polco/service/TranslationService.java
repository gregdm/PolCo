package com.gregdm.polco.service;

import com.gregdm.polco.domain.*;
import com.gregdm.polco.repository.BadWordRepository;
import com.gregdm.polco.repository.GoodWordRepository;
import com.gregdm.polco.repository.SearchBadWordRepository;
import com.gregdm.polco.repository.SearchGoodWordRepository;
import com.gregdm.polco.service.ImportXML.DicoSAXParser;
import liquibase.util.csv.CSVReader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class TranslationService {

    private final Logger log = LoggerFactory.getLogger(TranslationService.class);

    @Inject
    private BadWordRepository badWordRepository;

    @Inject
    private GoodWordRepository goodWordRepository;

    @Inject
    private SearchGoodWordRepository searchGoodWordRepository;

    @Inject
    private SearchBadWordRepository searchBadWordRepository;

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

    public String translateText(String text){
        //TODO GREG keep the uppercase in word
        //Put into set
        String tokens[] = text.split("\\s+");
        String textTranslated = new String(text);

        for(int i=0; i<tokens.length;i++){
            List<BadWord> badWordFind = searchBadWordRepository.findByValue(tokens[i].trim().toLowerCase());
            if(!CollectionUtils.isEmpty(badWordFind)){
                BadWord badWord = badWordFind.iterator().next();
                String toReplace = badWord.getTranslations().iterator().next().getValue() ;
                if(StringUtils.isNotBlank(toReplace)) {
                    textTranslated = textTranslated.replace(tokens[i], toReplace);
                }
            }
        }

        return textTranslated;
    }

    private String translateExpression(String text){
        List<Expression> expressions = expressionService.findAll();
        String newString = text;
        for(Expression expression : expressions){
            if(text.contains(expression.getValue())){
                newString.replace(expression.getValue(), expression.getExpressionTranss().iterator().next().getValue());
            }
        }
        return newString;
    }

    private String findTraduction(String s){
        List<Noun> nouns = nounService.findByValue(s);
        List<Adjective> adjectives = adjectiveService.findByValue(s);
        List<Interjection> interjections = interjectionService.findByValue(s);
        List<Verb> verbs = verbService.findByValue(s);

        if(collectionsEmpty(nouns, adjectives,interjections,verbs)){
            return s;
        }else {
            if(onlyOneCollectionNotEmpty(nouns, adjectives,interjections,verbs)){
                if(!CollectionUtils.isEmpty(nouns)){
                    return nouns.iterator().next().getTranslationss().iterator().next().getValue();
                }
                if(!CollectionUtils.isEmpty(adjectives)){
                    return adjectives.iterator().next().getTranslationss().iterator().next().getValue();
                }
                if(!CollectionUtils.isEmpty(verbs)){
                    return verbs.iterator().next().getTranslationss().iterator().next().getValue();
                }
                if(!CollectionUtils.isEmpty(interjections)){
                    return interjections.iterator().next().getTranslationss().iterator().next().getValue();
                }
            }
            return s;
        }
    }

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        //TODO GREG Add header improve with category of word
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

    public String importXML(String path){
        //TODO GREG A ameliorer faire un batch avec des commit flush par paquet dans hibernate

        //TODO GREG Arriver a récupere les nouveaux mots en temps réel du parser XML (peut être pas)
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DicoSAXParser dicoSAXParser = new DicoSAXParser();

            //saxParser.parse(file.getInputStream(), dicoSAXParser);
            saxParser.parse(path, dicoSAXParser);

            dicoSAXParser.getVerbList().forEach(n -> verbService.add(n));
            dicoSAXParser.getAdverbList().forEach(n -> adverbService.add(n));
            dicoSAXParser.getInterjectionList().forEach(n -> interjectionService.add(n));
            dicoSAXParser.getNominalDetList().forEach(n -> nominalDetService.add(n));
            dicoSAXParser.getPrefixList().forEach(n -> prefixService.add(n));
            dicoSAXParser.getPrepositionList().forEach(n -> prepositionService.add(n));
            dicoSAXParser.getNounList().forEach(n -> nounService.add(n));
            dicoSAXParser.getAdjectiveList().forEach(n -> adjectiveService.add(n));

        } catch (Exception e){
            e.printStackTrace();
        }
        return "XML";
    }

    private boolean onlyOneCollectionNotEmpty(Collection... collections){
        int nbCollectionNotEmpty = 0;
        for(int i=0;i<collections.length;i++){
            if(!CollectionUtils.isEmpty(collections[i])){
                nbCollectionNotEmpty ++;
            }
        }

        if(nbCollectionNotEmpty==1){
            return true;
        } else {
            return false;
        }
    }

    private boolean collectionsEmpty(Collection... collections){
        for(int i=0;i<collections.length;i++){
            if(!CollectionUtils.isEmpty(collections[i])){
                return false;
            }
        }
        return true;
    }

    public String importCSV(MultipartFile file){
        if (!file.isEmpty()) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
            String[] row = null;
            while((row = reader.readNext()) != null) {
                if(row.length>1){
                    BadWord badWord = new BadWord();
                    List<BadWord> badWordFind = searchBadWordRepository.findByValue(row[0].trim().toLowerCase());
                    if(CollectionUtils.isEmpty(badWordFind)){
                        if(StringUtils.isBlank(row[0])){
                            break;
                        }
                        badWord.setValue(row[0].trim().toLowerCase());
                        badWord.setType("NONE");
                        badWord = badWordRepository.save(badWord);
                    } else {
                        badWord = badWordFind.iterator().next();
                    }

                    for(int i=1; i<row.length-1; i++) {
                        if(StringUtils.isBlank(row[i])){
                            break;
                        }

                        boolean goodWordPresent = false;

                        //Si la traduction est déjà présente
                        for(GoodWord good : badWord.getTranslations()){
                            if(good.getValue().equals(row[i])){
                                goodWordPresent = true;
                                break;
                            }
                        }

                        //Sinon on l'ajoute au BadWord
                        if(!goodWordPresent){
                            GoodWord goodWord = new GoodWord();
                            goodWord.setValue(row[i].trim().toLowerCase());
                            goodWord.setLevel("NORMAL");
                            goodWord.setBadWord(badWord);
                            goodWordRepository.save(goodWord);
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fILE Greg";
    } else {
        return "You failed to upload " + file.getName() + " because the file was empty.";
    }
    }
}
