package com.gregdm.polco.service;

import com.google.common.collect.Lists;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.domain.util.EnumWordType;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test class for the UserResource REST controller.
 *
 * @see com.gregdm.polco.service.UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OpenNPLTest {

    private POSModel modelDetectionType;

    private POSTaggerME tagger;

    @Test
    public void testRemoveOldPersistentTokens() throws IOException, URISyntaxException {
//        String paragraph = "Oh "
//                           + "Boum! Andréa, Je l'aime enormement et lentement! J'avais bien aimé le truc. Pourquoi on est contrôlé, par la police, 17 fois par jour? Mais parce que la plupart des trafiquants sont noirs "
//                           + "et arabes; c'est comme ça, c'est un fait. C'est un arabe! "
//                           + "La discrimination c'est la vie!";
//
        String paragraph = "Publier <<<systématiquement >>>>> systématiquement et  systématiquement les rapports d’analyse des offres, "
                           + "faire des avis d’avenants systématiquement et créer " + "un référé avenants, "
                           + "centraliser les achats… Une note réalisée pour le Conseil "
                           + "d’analyse économique, " + "présentée le 15 avril, fait dix "
                           + "recommandations au gouvernement pour renforcer l’efficacité "
                           + "de la" + " commande publique." + "Philharmonie de Paris, "
                           + "autoroutes, eau, hôpital sud-francilien. Au regard de ces projets, "
                           + "" + "la commande publique semble inefficace. En tout cas, leurs coûts"
                           + " élevés ou leurs dérapages " + "financiers interrogent. Les auteurs"
                           + " d’une note faite ce mois d’avril pour le Conseil d’analyse" +
                           " économique (le CAE est une instance de conseil au gouvernement "
                           + "en matière économique), présentée" + " à la presse le 15 avril,"
                           + " ont pris ces projets comme exemple pour justifier la réflexion"
                           + " sur les" + " outils de la commande publique que sont aujourd’hui"
                           + " les marchés publics, les délégations de " + "service public et"
                           + " les partenariats public-privé. Les universitaires Stéphane Saussier"
                           + " "
                           + "(Institut d’administration des entreprises de Paris I Panthéon-Sorbonne)"
                           + " et Jean Tirole "
                           + "(Toulouse School of Economics et Institue for Advanced Study"
                           + " in Toulouse, membre du CAE), "
                           + "en marge de la transposition des directives européennes "
                           + "sur les marchés publics, font dix"
                           + " propositions pour « renforcer l’efficacité de "
                           + "la commande publique ». Celles-ci ne concernent"
                           + " que les gros contrats d’un montant "
                           + "supérieur aux seuils européens.";

        InputStream isSent = getClass().getClassLoader().getResourceAsStream("fr-sent.bin");
        SentenceModel modelDetectionSentence = new SentenceModel(isSent);
        SentenceDetectorME sdetector = new SentenceDetectorME(modelDetectionSentence);

        modelDetectionType = new POSModelLoader()
            .load(new File(getClass().getClassLoader().getResource("fr-pos.bin").toURI()));

        tagger = new POSTaggerME(modelDetectionType);

        for (String sent : sdetector.sentDetect(paragraph)) {
            //System.out.println(sent);
            System.out.println(
                translateListToString(this.translateWords(this.getWordsWithWordType(sent))));
        }

        isSent.close();
    }

    private String translateListToString(List<WordValidation> words){
        StringBuilder translateSent = new StringBuilder();
        for(WordValidation word : words){

            String wordTranslation = word.getTranslation();
            if(Character.isUpperCase(word.getValue().charAt(0))){
                wordTranslation = StringUtils.capitalize(wordTranslation);
            }
            translateSent.append(wordTranslation + " ");
        }
        return translateSent.toString();
    }

    private List<WordValidation> translateWords(List<WordValidation> words) {
        Pattern pattern = Pattern.compile("[\\p{Punct}&&[^-']]");
        //Mettre de coté la ponctuation, afin de traduire le mot, puis on remettre la ponctuation
        for(WordValidation word : words) {
            String wordToTranslate = word.getValue();
            Matcher matcher = pattern.matcher(wordToTranslate);
            if(matcher.find()) {
                String[] splits = pattern.split(wordToTranslate);
                String splitter = matcher.toMatchResult().group();
                if(splits.length == 1){
                    word.setTranslation(splits[0] + splitter);
                } else {
                    //On ne gère pas le cas d'un ponctuation pas classique
                    word.setTranslation(wordToTranslate);
                }
            } else {
                word.setTranslation(word.getValue());
            }
        }
        return words;
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
}

