package com.gregdm.polco.service;

import com.gregdm.polco.domain.*;
import com.gregdm.polco.service.ImportXML.DicoSAXParser;
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
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class TranslationService {

    private final Logger log = LoggerFactory.getLogger(TranslationService.class);

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
            List<Expression> expressionFind = expressionService.findByValue(tokens[i].trim().toLowerCase());
            if(!CollectionUtils.isEmpty(expressionFind)){
                Expression expression = expressionFind.iterator().next();
                String toReplace = expression.getExpressionTranss().iterator().next().getValue() ;
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

}
