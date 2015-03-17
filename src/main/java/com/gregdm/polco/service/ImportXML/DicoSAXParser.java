package com.gregdm.polco.service.ImportXML;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.domain.NominalDet;
import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.domain.Prefix;
import com.gregdm.polco.domain.Preposition;
import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.service.NounService;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Greg on 09/03/2015.
 */
public class DicoSAXParser extends DefaultHandler {

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
    @Inject
    private NounService nounService;
    private List<Noun> nounList = new LinkedList<>();

    private List<Verb> verbList = new LinkedList<>();
    private List<Adjective> adjectiveList = new LinkedList<>();
    private List<Adverb> adverbList = new LinkedList<>();
    private List<Prefix> prefixList = new LinkedList<>();
    private List<Interjection> interjectionList = new LinkedList<>();
    private List<Preposition> prepositionList = new LinkedList<>();
    private List<NominalDet> nominalDetList = new LinkedList<>();

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
                entryTemp.compound = attributes.getValue(0);
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
                if (StringUtils.isNotBlank(e.compound)) {
                    noun.setCompound(e.compound);
                } else {
                    noun.setCompound(NONE);
                }
                if (StringUtils.isNotBlank(i.map.get(GENDER))) {
                    noun.setGender(i.map.get(GENDER));
                } else {
                    noun.setGender(NONE);
                }
                if (StringUtils.isNotBlank(i.map.get(NUMBER))) {
                    noun.setNumber(i.map.get(NUMBER));
                } else {
                    noun.setNumber(NONE);
                }
                nounList.add(noun);
            }
        } else if (VERB.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Verb verb = new Verb();
                verb.setValue(i.form);

                if (StringUtils.isNotBlank(i.map.get(TENSE))) {
                    verb.setTense(i.map.get(TENSE));
                } else {
                    verb.setTense(NONE);
                }
                if (StringUtils.isNotBlank(i.map.get(PERSON))) {
                    verb.setPerson(i.map.get(PERSON));
                } else {
                    verb.setPerson(NONE);
                }
                if (StringUtils.isNotBlank(i.map.get(NUMBER))) {
                    verb.setNumber(i.map.get(NUMBER));
                } else {
                    verb.setNumber(NONE);
                }
                verbList.add(verb);
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
                adjectiveList.add(adj);
            }
        } else if (PREP.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Preposition prep = new Preposition();
                prep.setValue(i.form);
                prepositionList.add(prep);
            }
        } else if (INTJ.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Interjection intj = new Interjection();
                intj.setValue(i.form);
                interjectionList.add(intj);
            }
        } else if (PREFIX.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Prefix prefix = new Prefix();
                prefix.setValue(i.form);
                prefixList.add(prefix);
            }
        } else if (ADVERB.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                Adverb adv = new Adverb();
                adv.setValue(i.form);
                adverbList.add(adv);
            }
        } else if (NOMINALDET.equals(e.pos)) {
            for (Inflected i : e.inflecteds) {
                NominalDet nomDet = new NominalDet();
                nomDet.setValue(i.form);
                nominalDetList.add(nomDet);
            }
        }
    }

    public List<Noun> getNounList() {
        return nounList;
    }


    public List<Verb> getVerbList() {
        return verbList;
    }


    public List<Adjective> getAdjectiveList() {
        return adjectiveList;
    }


    public List<Adverb> getAdverbList() {
        return adverbList;
    }


    public List<Prefix> getPrefixList() {
        return prefixList;
    }


    public List<Interjection> getInterjectionList() {
        return interjectionList;
    }


    public List<Preposition> getPrepositionList() {
        return prepositionList;
    }

    public List<NominalDet> getNominalDetList() {
        return nominalDetList;
    }

};
