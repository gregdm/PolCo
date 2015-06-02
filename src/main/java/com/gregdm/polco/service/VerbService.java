package com.gregdm.polco.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.domain.VerbTrans;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.VerbRepository;
import com.gregdm.polco.repository.VerbTransRepository;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

@Service
@Transactional
public class VerbService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(VerbService.class);

    @Inject
    private VerbRepository verbRepository;

    @Inject
    private VerbTransRepository verbTransRepository;


    public List<VerbTrans> findAllVerbTrans() {
        return verbTransRepository.findAll();
    }

    @CacheEvict(value = { "multimapTransVerb", "multimapVerbTrans" }, allEntries = true)
    public boolean add(WordValidation word) {
        if (StringUtils.isBlank(word.getValue())) {
            return false;
        }
        Verb verb = new Verb();
        verb.setValue(word.getValue());
        verb.setPerson(word.getPerson());
        verb.setTense(word.getTense());
        verb.setNumber(word.getNumber());

        verb = findOrCreate(verb);

        VerbTrans verbTrans = new VerbTrans();
        verbTrans.setValue(word.getTranslation());
        verbTrans.setVerb(verb);
        if (CollectionUtils.isEmpty(
            verbTransRepository.findByValueAndVerb(verbTrans.getValue(), verbTrans.getVerb()))) {
            verbTrans.lowerStrings();
            verbTransRepository.save(verbTrans);
            return true;
        }
        return false;
    }

    @Cacheable("multimapVerbTrans")
    public Multimap getMultimapTranslation(){

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllVerbTrans().forEach(
            e -> expressions.put(e.getVerb().getValue(),e.getValue()));

        return expressions;
    }

    @Cacheable("multimapTransVerb")
    public Multimap getMultimapTranslationValue(){

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllVerbTrans().forEach(
            e -> expressions.put(e.getValue(),e.getVerb().getValue()));

        return expressions;
    }

    public List<Verb> findByValue(String value) {
        return verbRepository.findByValue(this.stringBDD(value));
    }

    public List<Verb> findVerb(Verb verb) {
        if (verb != null &&
            StringUtils.isNotBlank(verb.getValue()) &&
            StringUtils.isNotBlank(verb.getNumber()) &&
            StringUtils.isNotBlank(verb.getTense()) &&
            StringUtils.isNotBlank(verb.getPerson())) {
            return verbRepository.findByValueAndPersonAndTenseAndNumber(
                stringBDD(verb.getValue()), stringBDD(verb.getPerson()),
                stringBDD(verb.getTense()), stringBDD(verb.getNumber()));
        } else if (verb != null &&
                   StringUtils.isNotBlank(verb.getValue())) {
            return verbRepository.findByValue(verb.getValue());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Verb findOrCreate(Verb verb) {
        if (verb == null && StringUtils.isNotBlank(verb.getValue())) {
            log.error("Verb is null");
            throw new BadObjectException("Verb is invalide");
        }
        verb.lowerStrings();

        List<Verb> VerbList = this.findVerb(verb);
        if (CollectionUtils.isEmpty(VerbList)) {
            return verbRepository.save(verb);
        } else {
            log.info("Verb isn't findOrCreate because he already existe null");
            return VerbList.iterator().next();
        }
    }


}
