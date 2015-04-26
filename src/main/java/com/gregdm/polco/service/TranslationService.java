package com.gregdm.polco.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
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
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.domain.util.EnumWordType;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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
    private Multimap<String, String> allAdverbTrans;
    private POSModel modelDetectionType;
    private POSTaggerME tagger;
    private Pattern patternPunct = Pattern.compile("[\\p{Punct}&&[^-']]");

    public String translateText(String textInitial) throws URISyntaxException, IOException {

        initCollections();
        String textTranslated = new String(textInitial);
        textTranslated = translateExpressions(textTranslated);
        textTranslated = translateWords(textTranslated);
        return textTranslated;
    }

    private String translateListToString(List<WordValidation> words){
        StringBuilder translateSent = new StringBuilder();
        for(WordValidation word : words){

            String wordTranslation = word.getTranslation();
            if(Character.isUpperCase(word.getValue().charAt(0))){
                wordTranslation = org.apache.commons.lang.StringUtils.capitalize(wordTranslation);
            }
            translateSent.append(wordTranslation + " ");
        }
        return translateSent.toString();
    }

    private String doTranslation(String value, String wordType){
        Collection<String> nouns = allNounTrans.get(value);
        Collection<String> verbs = allVerbTrans.get(value);
        Collection<String> adjectives = allAdjectiveTrans.get(value);
        Collection<String> adverbs = allAdverbTrans.get(value);

        switch(wordType){
            case "NOUN" :
                if(CollectionUtils.isEmpty(nouns)){
                    return value;
                }
                return getRandomString(nouns);

            case "VERB" :
                if(CollectionUtils.isEmpty(verbs)){
                    return value;
                }
                return getRandomString(verbs);

            case "ADJECTIVE" :
                if(CollectionUtils.isEmpty(adjectives)){
                    return value;
                }
                return getRandomString(adjectives);

            case "ADVERB" :
                if(CollectionUtils.isEmpty(adverbs)){
                    return value;
                }
                return getRandomString(adverbs);

            case "NONE" :

                if (collectionsEmpty(nouns, adjectives, adverbs, verbs)) {
                    return value;
                } else {
                    if (onlyOneCollectionNotEmpty(nouns, adjectives, adverbs, verbs)) {
                        if (!CollectionUtils.isEmpty(nouns)) {
                            return getRandomString(nouns);
                        }
                        if (!CollectionUtils.isEmpty(adjectives)) {
                            return getRandomString(adjectives);
                        }
                        if (!CollectionUtils.isEmpty(verbs)) {
                            return getRandomString(verbs);
                        }
                        if (!CollectionUtils.isEmpty(adverbs)) {
                            return getRandomString(adverbs);
                        }
                    }
                }
            return value;

        }
            return value;
    }

    private WordValidation translateWordValidation(WordValidation word){
        String wordToTranslate = word.getValue();
        //Mettre de coté la ponctuation, afin de traduire le mot, puis on remettre la ponctuation
        Matcher matcher = patternPunct.matcher(wordToTranslate);
        if(matcher.find()) {
            String[] splits = patternPunct.split(wordToTranslate);
            String splitter = matcher.toMatchResult().group();
            if(splits.length == 1){
                word.setTranslation(doTranslation(splits[0], word.getWordType()) + splitter);
            } else {
                //On ne gère pas le cas d'un ponctuation pas classique
                word.setTranslation(wordToTranslate);
            }
        } else {
            word.setTranslation(doTranslation(word.getValue(), word.getWordType()));
        }
        return word;
    }

    private List<WordValidation> getWordsWithWordType(String sent)
        throws IOException, URISyntaxException {

        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
            .tokenize(sent);
        String[] tags = tagger.tag(whitespaceTokenizerLine);
        POSSample sample = new POSSample(whitespaceTokenizerLine, tags);

        List<WordValidation> words = Lists.newArrayList();
        //Transform tags in Word Type
        for (int i = 0; i < sample.getSentence().length; i++) {
            String tag = sample.getTags()[i];
            EnumWordType wordType;
            switch (tag) {
                case "NC":
                    wordType = EnumWordType.NOUN;
                    break;
                case "V":
                    wordType = EnumWordType.VERB;
                    break;
                case "VPP":
                    wordType = EnumWordType.VERB;
                    break;
                case "VINF":
                    wordType = EnumWordType.VERB;
                    break;
                case "ADJ":
                    wordType = EnumWordType.ADJECTIVE;
                    break;
                case "ADV":
                    wordType = EnumWordType.ADVERB;
                    break;
                default:
                    wordType = EnumWordType.NONE;
                    break;
            }
            String key = sample.getSentence()[i];
            words.add(new WordValidation(key, wordType.name()));
        }
        return words;
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
        this.allAdverbTrans = adverbService.getMultimapTranslation();
    }

    private String translateWords(String text) throws URISyntaxException, IOException {

        InputStream isSent = getClass().getClassLoader().getResourceAsStream("fr-sent.bin");
        SentenceModel modelDetectionSentence = new SentenceModel(isSent);
        SentenceDetectorME sdetector = new SentenceDetectorME(modelDetectionSentence);

        modelDetectionType = new POSModelLoader()
            .load(new File(getClass().getClassLoader().getResource("fr-pos.bin").toURI()));
        tagger = new POSTaggerME(modelDetectionType);

        StringBuilder newText =  new StringBuilder();
        for (String sent : sdetector.sentDetect(text)) {
            //System.out.println(sent);
            List<WordValidation> wordsWithWordType = this.getWordsWithWordType(sent);
            wordsWithWordType.forEach(word -> word = translateWordValidation(word));
            newText.append(translateListToString(wordsWithWordType));
        }

        isSent.close();
        return newText.toString();
    }

    private String translateExpressions(String text) {
        //Replace expression translation
        for(String key : this.expressionTrans.keySet()){
            //If text start with uppercase
            //Boundary, check if an entire word and not a substring ex: IN and INput
            String regexExpression = "\\b"+ key + "\\b";
            Pattern pInsensitive = Pattern.compile(regexExpression, Pattern.CASE_INSENSITIVE);
            Pattern pSensitive = Pattern.compile(regexExpression);

            Matcher matcherSensitive = pSensitive.matcher(text);
            StringBuffer sb = new StringBuffer();
            //Get different translation for the same expression
            while(matcherSensitive.find()) {
                String randomString = this.getRandomString(this.expressionTrans.get(key));
                matcherSensitive.appendReplacement(sb, randomString);
            }
            matcherSensitive.appendTail(sb);
            Matcher matcherInsensitive = pInsensitive.matcher(sb.toString());
            sb = new StringBuffer(); // ADDED
            while(matcherInsensitive.find()) {
                String randomString = this.getRandomString(this.expressionTrans.get(key));
                matcherInsensitive.appendReplacement(sb,StringUtils.capitalize(randomString));
            }
            matcherInsensitive.appendTail(sb);
            text = sb.toString();
        }
        return text;
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
