package com.gregdm.polco.service;

import com.gregdm.polco.domain.BadWord;
import com.gregdm.polco.domain.GoodWord;
import com.gregdm.polco.repository.BadWordRepository;
import com.gregdm.polco.repository.GoodWordRepository;
import com.gregdm.polco.repository.SearchBadWordRepository;
import com.gregdm.polco.repository.SearchGoodWordRepository;
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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public String translateText(String text){
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

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

    public String importXML(MultipartFile file){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();



            DefaultHandler handler = new DefaultHandler() {
                boolean bfname = false;
                boolean blname = false;
                boolean bnname = false;
                boolean bsalary = false;


                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {

                    System.out.println("Start Element :" + qName);

                    if (qName.equalsIgnoreCase("FIRSTNAME")) {
                        bfname = true;
                    }

                    if (qName.equalsIgnoreCase("LASTNAME")) {
                        blname = true;
                    }

                    if (qName.equalsIgnoreCase("NICKNAME")) {
                        bnname = true;
                    }

                    if (qName.equalsIgnoreCase("SALARY")) {
                        bsalary = true;
                    }

                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {

                    System.out.println("End Element :" + qName);

                }

                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bfname) {
                        System.out.println("First Name : " + new String(ch, start, length));
                        bfname = false;
                    }

                    if (blname) {
                        System.out.println("Last Name : " + new String(ch, start, length));
                        blname = false;
                    }

                    if (bnname) {
                        System.out.println("Nick Name : " + new String(ch, start, length));
                        bnname = false;
                    }

                    if (bsalary) {
                        System.out.println("Salary : " + new String(ch, start, length));
                        bsalary = false;
                    }

                }

            };

            saxParser.parse(file.getInputStream(), handler);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "XML";
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
