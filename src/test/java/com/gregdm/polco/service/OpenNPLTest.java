package com.gregdm.polco.service;

import com.gregdm.polco.Application;
import com.gregdm.polco.domain.PersistentToken;
import com.gregdm.polco.domain.User;
import com.gregdm.polco.repository.PersistentTokenRepository;
import com.gregdm.polco.repository.UserRepository;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

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


    @Test
    public void testRemoveOldPersistentTokens() throws IOException, URISyntaxException {
        String paragraph = "Pourquoi on est contrôlé, par la police, 17 fois par jour? Mais parce que la plupart des trafiquants sont noirs "
                           + "et arabes; c'est comme ça, c'est un fait. C'est un arabe! "
                           + "La discrimination c'est la vie!";

        InputStream isSent = getClass().getClassLoader().getResourceAsStream("fr-sent.bin");
        SentenceModel modelDetectionSentence = new SentenceModel(isSent);
        SentenceDetectorME sdetector = new SentenceDetectorME(modelDetectionSentence);


        for (String sent : sdetector.sentDetect(paragraph)){
          System.out.println(sent);
            this.postag(sent);
        }

        isSent.close();
    }

    private Map<String, String> postag(String sent) throws IOException, URISyntaxException {


        POSModel modelDetectionType = new POSModelLoader()
            .load(new File(getClass().getClassLoader().getResource("fr-pos.bin").toURI()));

        POSTaggerME tagger = new POSTaggerME(modelDetectionType);

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                .tokenize(sent);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            LinkedHashMap<String, String> words = new LinkedHashMap<>();
            for(int i=0;i<sample.getSentence().length;i++){
                words.put(sample.getSentence()[i], sample.getTags()[i]);
                System.out.println(sample.getSentence()[i] +" : "+ sample.getTags()[i]);
            }
            return words;
    }
}

