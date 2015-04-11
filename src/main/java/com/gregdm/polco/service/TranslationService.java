package com.gregdm.polco.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.domain.ExpressionTrans;
import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.domain.InterjectionTrans;
import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.domain.VerbTrans;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

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
    private InterjectionService interjectionService;
    @Inject
    private AdverbService adverbService;
    @Inject
    private ExpressionService expressionService;

    private Multimap<String, String> expressionTrans;
    private Multimap<String, String> allNounTrans;
    private Multimap<String, String> allAdjectiveTrans;
    private Multimap<String, String> allVerbTrans;
    private Multimap<String, String> allInterjectionTrans;
    private Multimap<String, String> allAdverbTrans;

    public String translateText(String textInitial) {

        initCollections();
        String textTranslated = new String(textInitial);
        textTranslated = translateExpressions(textTranslated);
        textTranslated = translateWords(textTranslated);

        //Put into set
//        String tokens[] = textInitial.split("\\s+");
//        String textTranslated = new String(textInitial);
//
//        for (int i = 0; i < tokens.length; i++) {
//            List<Expression>
//                expressionFind =
//                expressionService.findByValue(tokens[i].trim().toLowerCase());
//            if (!CollectionUtils.isEmpty(expressionFind)) {
//                Expression expression = expressionFind.iterator().next();
//                String toReplace = expression.getExpressionTranss().iterator().next().getValue();
//                if (StringUtils.isNotBlank(toReplace)) {
//                    textTranslated = textTranslated.replace(tokens[i], toReplace);
//                }
//            }
//        }

        return textTranslated;
    }

    private String getRandomString(Collection<String> c){
        int size = c.size();
        int n = new Random().nextInt(size);
        return c.toArray(new String[size])[n];
    }

    private void initCollections(){
        this.expressionTrans = expressionService.getMultimapTranslation();
        this.allNounTrans = nounService.getMultimapTranslation();
        this.allAdjectiveTrans = adjectiveService.getMultimapTranslation();
        this.allVerbTrans = verbService.getMultimapTranslation();
        this.allInterjectionTrans = interjectionService.getMultimapTranslation();
        this.allAdverbTrans = adverbService.getMultimapTranslation();
    }

    private String translateWords(String text) {

        return text;
    }

    private String translateExpressions(String text) {

        //Replace expression translation
        for(String key : this.expressionTrans.keySet()){
            //If text start with uppercase
            //Boundary, check if an entire word and not a substring ex: IN and INput
            Pattern pInsensitive = Pattern.compile("\\b"+key+"\\b", Pattern.CASE_INSENSITIVE);
            Pattern pSensitive = Pattern.compile("\\b"+key+"\\b");

            if(pSensitive.matcher(text).find()) {
                String randomString = this.getRandomString(this.expressionTrans.get(key));
                text = text.replace(key, randomString);
            } else if(pInsensitive.matcher(text).find()) {
                String randomString = this.getRandomString(this.expressionTrans.get(key));
                text = text.replaceAll("(?i)"+key, StringUtils.capitalize(randomString));
            }
        }
        return text;
    }

    private String findTraduction(String s) {
        List<Noun> nouns = nounService.findByValue(s);
        List<Adjective> adjectives = adjectiveService.findByValue(s);
        List<Interjection> interjections = interjectionService.findByValue(s);
        List<Verb> verbs = verbService.findByValue(s);

        if (collectionsEmpty(nouns, adjectives, interjections, verbs)) {
            return s;
        } else {
            if (onlyOneCollectionNotEmpty(nouns, adjectives, interjections, verbs)) {
                if (!CollectionUtils.isEmpty(nouns)) {
                    return nouns.iterator().next().getTranslationss().iterator().next().getValue();
                }
                if (!CollectionUtils.isEmpty(adjectives)) {
                    return adjectives.iterator().next().getTranslationss().iterator().next()
                        .getValue();
                }
                if (!CollectionUtils.isEmpty(verbs)) {
                    return verbs.iterator().next().getTranslationss().iterator().next().getValue();
                }
                if (!CollectionUtils.isEmpty(interjections)) {
                    return interjections.iterator().next().getTranslationss().iterator().next()
                        .getValue();
                }
            }
            return s;
        }
    }

    private boolean onlyOneCollectionNotEmpty(Collection... collections) {
        int nbCollectionNotEmpty = 0;
        for (int i = 0; i < collections.length; i++) {
            if (!CollectionUtils.isEmpty(collections[i])) {
                nbCollectionNotEmpty++;
            }
        }

        if (nbCollectionNotEmpty == 1) {
            return true;
        } else {
            return false;
        }
    }

    private boolean collectionsEmpty(Collection... collections) {
        for (int i = 0; i < collections.length; i++) {
            if (!CollectionUtils.isEmpty(collections[i])) {
                return false;
            }
        }
        return true;
    }

}
