package com.gregdm.polco.service;

import com.gregdm.polco.domain.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Greg on 04/03/2015.
 */
public class MainTest {


    public static void main(java.lang.String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // made new external class extending DefaultHandler
            MySaxParser handler = new MySaxParser();


            saxParser.parse("C:\\Users\\Greg\\Desktop\\dela-fr-public-u8.dic.xml",
                handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class MySaxParser extends DefaultHandler {

    public static final String NOUN = "noun";
    public static final String ADJ = "adj";
    public static final String VERB = "verb";
    public static final String PREP = "prep";
    public static final String PREFIX = "prefix";
    public static final String ADVERB = "adverb";
    public static final String INTJ = "intj";
    public static final String NOMINALDET = "nominaldet";

    public static final String ENTRY = "entry";
    public static final String INFLECTED = "inflected";
    public static final String FORM = "form";
    public static final String DICO = "dico";
    public static final String POS = "pos";
    public static final String COMPOUND = "compound";
    public static final String GENDER = "gender";
    public static final String NUMBER = "number";
    public static final String NONE = "none";
    public static final String TENSE = "tense";
    public static final String PERSON = "person";

    Entry entryTemp;
    Inflected inflectedTemp;
    String tmpValue;
    boolean lvlEntry = true;

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes)
        throws SAXException {

        if (DICO.equals(qName)) {
        } else if (ENTRY.equals(qName)) {
            entryTemp = new Entry();
        } else if (POS.equals(qName)) {
            entryTemp.pos = attributes.getValue(0);
        } else if (INFLECTED.equals(qName)) {
            lvlEntry = false;
            inflectedTemp = new Inflected();
        } else if (FORM.equals(qName)) {
            //In characters
        } else {
            if (lvlEntry) {
                entryTemp.compound=attributes.getValue(0);
            } else {
                inflectedTemp.map.put(attributes.getValue(0), attributes.getValue(1));
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws
        SAXException {

        // System.out.println("End  Element :" + qName);

        if (ENTRY.equals(qName)) {
            transformEntityInObjects(entryTemp);
            lvlEntry = true;
        } else if (INFLECTED.equals(qName)) {
            entryTemp.inflecteds.add(inflectedTemp);
        } else if (FORM.equals(qName)) {
            if (!lvlEntry) {
                inflectedTemp.form = tmpValue;
                tmpValue = null;
            }
        }
    }

    public void characters(char ch[], int start, int length) throws
        SAXException {
        tmpValue = new String(ch, start, length);
    }

    // Try avec des maps pour les balises
    public void transformEntityInObjects(Entry e) {
        if (NOUN.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Noun noun = new Noun();
                noun.setValue(i.form);
                if(StringUtils.isNotBlank(e.compound)) {
                    noun.setCompound(e.compound);
                } else {
                    noun.setCompound(NONE);
                }
                if (StringUtils.isNotBlank(i.map.get(GENDER))) {
                    noun.setGender(i.map.get(GENDER));
                }
                if (StringUtils.isNotBlank(i.map.get(NUMBER))) {
                    noun.setNumber(i.map.get(NUMBER));
                }
                System.out.println(noun.toString());
            }
        } else if (VERB.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Verb verb = new Verb();
                verb.setValue(i.form);

                if (StringUtils.isNotBlank(i.map.get(TENSE))) {
                    verb.setTense(i.map.get(TENSE));
                }
                if (StringUtils.isNotBlank(i.map.get(PERSON))) {
                    verb.setPerson(i.map.get(PERSON));
                }
                if (StringUtils.isNotBlank(i.map.get(NUMBER))) {
                    verb.setNumber(i.map.get(NUMBER));
                }
                System.out.println(verb.toString());
            }
        } else if (ADJ.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Adjective adj = new Adjective();
                adj.setValue(i.form);

                if (StringUtils.isNotBlank(i.map.get(GENDER))) {
                    adj.setGender(i.map.get(GENDER));
                } else {
                    adj.setGender(NONE);
                }
                if (StringUtils.isNotBlank(i.map.get(NUMBER))) {
                    adj.setNumber(i.map.get(NUMBER));
                } else {
                    adj.setNumber(NONE);
                }
                System.out.println(adj.toString());
            }
        } else if (PREP.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Preposition prep = new Preposition();
                prep.setValue(i.form);
                System.out.println(prep.toString());
            }
        }  else if (INTJ.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Interjection intj = new Interjection();
                intj.setValue(i.form);
                System.out.println(intj.toString());
            }
        }  else if (PREFIX.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Prefix prefix = new Prefix();
                prefix.setValue(i.form);
                System.out.println(prefix.toString());
            }
        }  else if (ADVERB.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Adverb adv = new Adverb();
                adv.setValue(i.form);
                System.out.println(adv.toString());
            }
        }   else if (NOMINALDET.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                NominalDet nomDet = new NominalDet();
                nomDet.setValue(i.form);
                System.out.println(nomDet.toString());
            }
        }
    }
};


class Entry {
    String pos = new String();
    String compound = new String();
    public List<Inflected> inflecteds = new ArrayList<Inflected>();
}


class Inflected {
    public String form;
    Map<String,String> map = new HashMap<>();
}

