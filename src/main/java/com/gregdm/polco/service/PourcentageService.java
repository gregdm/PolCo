package com.gregdm.polco.service;

import com.google.common.collect.Multimap;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.domain.Verb;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
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

        int nbMatch = 0;
        int nbTotalWord = text.split(" ").length;
        nbMatch += nbMatch(text, allNounTrans);
        nbMatch += nbMatch(text, allAdjectiveTrans);
        nbMatch += nbMatch(text, allAdverbTrans);
        nbMatch += nbMatch(text, allVerbTrans);
        nbMatch += nbMatch(text, allExpressionTrans);
        return "Pourcetage : " + nbMatch/nbTotalWord*100*3 + "%";
    }

    private int nbMatch(String text, Multimap<String,String> map){
        int occurences = 0;
        for(Map.Entry<String,String> entry : map.entries()){
            if(text.toLowerCase().contains(entry.getKey().toLowerCase())){
                occurences++;
            }
        }
        return occurences;
    }
}
