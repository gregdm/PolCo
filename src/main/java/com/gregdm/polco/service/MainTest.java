package com.gregdm.polco.service;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 04/03/2015.
 */
public class MainTest {

    public static final String NOUN = "noun";
    public static final String ADJ = "adj";
    public static final String VERB = "verb";
    public static final String PREP = "prep";
    public static final String PREFIX = "prefix";
    public static final String ADVERB = "adverb";
    public static final String INTJ = "intj";

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }

    public static void main(java.lang.String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // made new external class extending DefaultHandler
            DefaultHandler handler = new DefaultHandler() {
                int nbNoun = 0;
                int nbAdj = 0;
                int nbPrep = 0;
                int nbPrefix = 0;
                int nbAdverb = 0;
                int nbIntJ = 0;
                int nbVerb = 0;

                Object obj = new Object();
                List<Object> objects = new ArrayList<>();

                //If qname == entry
                //New Object with list Term<String, List<String,String>
                // Entry
                //   List<Balise>
                        // Balise
                        //  String
                        // List<QNameValue>
                //   List<inflected>
                        //  String
                        //  List<Balise>
                // AU moment de la fermeture de la balise créer les objects

                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {

                   // System.out.println("Start Element :" + qName);
                    int length = attributes.getLength();
                    for(int i = 0; i<length;i++){
                        String qName1 = attributes.getQName(i);
                        String value = attributes.getValue(i);
                        if(value.equals(NOUN)){
                            nbNoun++;
                        } else if(value.equals(ADJ)){
                            nbAdj++;
                        } else if(value.equals(VERB)){
                            nbVerb++;
                        }else if(value.equals(PREP)){
                            nbPrep++;
                        }else if(value.equals(PREFIX)){
                            nbPrefix++;
                        }else if(value.equals(ADVERB)){
                            nbAdverb++;
                        }else if(value.equals(INTJ)){
                            nbIntJ++;
                        }
                        //System.out.println("Name:" + qName1);
                        //System.out.println("Value :" + value);
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {

                    //System.out.println("End  Element :" + qName);
                    if(qName.equals("dico")){
                    System.out.println("Nb Noun  :" + nbNoun);
                    System.out.println("Nb Adj  :" + nbAdj);
                    System.out.println("Nb Verb  :" + nbVerb);
                    System.out.println("Nb Adverb  :" + nbAdverb);
                    System.out.println("Nb IntJ  :" + nbIntJ);
                    System.out.println("Nb Prep  :" + nbPrep);
                    System.out.println("Nb Prefix  :" + nbPrefix);

                    }

                }

                public void characters(char ch[], int start, int length) throws SAXException {


                        //System.out.println("Thing : " + new String(ch, start, length));

                }

            };

            saxParser.parse("C:\\Users\\Greg\\Desktop\\polco-test.xml", handler);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
