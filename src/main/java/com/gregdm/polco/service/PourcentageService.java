package com.gregdm.polco.service;

import com.google.common.collect.Multimap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import javax.inject.Inject;

@Service
@Transactional
public class PourcentageService {

    private final Logger log = LoggerFactory.getLogger(PourcentageService.class);

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

    private Multimap<String, String> allNounTrans;
    private Multimap<String, String> allAdjectiveTrans;
    private Multimap<String, String> allVerbTrans;
    private Multimap<String, String> allAdverbTrans;
    private Multimap<String, String> allExpressionTrans;


    public String getPourcentage(String text) {

        allNounTrans = nounService.getMultimapTranslationValue();
        allAdjectiveTrans = adjectiveService.getMultimapTranslationValue();
        allVerbTrans = verbService.getMultimapTranslationValue();
        allAdverbTrans = adverbService.getMultimapTranslationValue();
        allExpressionTrans = expressionService.getMultimapTranslationValue();

        int nbPolCo = getNbPolCo(text);
        int nbNoPolCo = getNbNoPolCo(text);

        double sum = nbPolCo - 1.5*nbNoPolCo;
        double nbTotalWord = text.split(" ").length;
        int result = Double.valueOf((sum*300)/nbTotalWord).intValue();

        return String.valueOf(result);
    }

    private int getNbPolCo(String text){
        int nbMatch = 0;
        nbMatch += nbPolCo(text, allNounTrans);
        nbMatch += nbPolCo(text, allAdjectiveTrans);
        nbMatch += nbPolCo(text, allAdverbTrans);
        nbMatch += nbPolCo(text, allVerbTrans);
        nbMatch += nbPolCo(text, allExpressionTrans);
        return nbMatch;
    }
    private int getNbNoPolCo(String text){
        int nbMatch = 0;
        nbMatch += nbNoPolCo(text, allNounTrans);
        nbMatch += nbNoPolCo(text, allAdjectiveTrans);
        nbMatch += nbNoPolCo(text, allAdverbTrans);
        nbMatch += nbNoPolCo(text, allVerbTrans);
        nbMatch += nbNoPolCo(text, allExpressionTrans);
        return nbMatch;
    }

    private int nbPolCo(String text, Multimap<String, String> map){
        int occurences = 0;
        for(Map.Entry<String,String> entry : map.entries()){
            if(text.toLowerCase().contains(entry.getKey().toLowerCase())){
                occurences++;
            }
        }
        return occurences;
    }
    private int nbNoPolCo(String text, Multimap<String, String> map){
        int occurences = 0;
        for(Map.Entry<String,String> entry : map.entries()){
            if(text.toLowerCase().contains(entry.getValue().toLowerCase())){
                occurences++;
            }
        }
        return occurences;
    }
}
