package com.gregdm.polco.service;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * Created by Greg on 04/03/2015.
 */
public class MainTest {

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

            DefaultHandler handler = new DefaultHandler() {
                boolean bfname = false;
                boolean blname = false;
                boolean bnname = false;
                boolean bsalary = false;


                public void startElement(String uri, String localName,String qName,
                                         Attributes attributes) throws SAXException {

                    System.out.println("Start Element :" + qName);
                    System.out.println("Start Element :" + uri);
                    System.out.println("Start Element :" + localName);
                    System.out.println("Start Element :" + attributes);
                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {

                    System.out.println("Start Element :" + qName);
                    System.out.println("Start Element :" + uri);
                    System.out.println("Start Element :" + localName);

                }

                public void characters(char ch[], int start, int length) throws SAXException {


                        System.out.println("Thing : " + new String(ch, start, length));

                }

            };

            saxParser.parse("C:\\Users\\Greg\\Desktop\\polco-test.xml", handler);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
