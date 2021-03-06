package com.gregdm.polco.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.gregdm.polco.domain.ExpressionTrans;
import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.NounRepository;
import com.gregdm.polco.repository.NounTransRepository;

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
public class NounService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(NounService.class);

    @Inject
    private NounRepository nounRepository;

    @Inject
    private NounTransRepository nounTransRepository;

    @CacheEvict(value = { "multimapTransNoun", "multimapNounTrans" }, allEntries = true)
    public boolean add(WordValidation word) {
        if (StringUtils.isBlank(word.getValue())) {
            return false;
        }
        Noun noun = new Noun();
        noun.setValue(word.getValue());
        noun.setCompound("noun");
        noun.setGender(word.getGender());
        noun.setNumber(word.getNumber());

        noun = findOrCreate(noun);

        NounTrans nounTrans = new NounTrans();
        nounTrans.setValue(word.getTranslation());
        nounTrans.setNoun(noun);
        if (CollectionUtils.isEmpty(
            nounTransRepository.findByValueAndNoun(nounTrans.getValue(), nounTrans.getNoun()))) {
            nounTrans.lowerStrings();
            nounTransRepository.save(nounTrans);
            return true;
        }
        return false;
    }

    @Cacheable("multimapNounTrans")
    public Multimap getMultimapTranslation(){

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllNounTrans().forEach(
            e -> expressions.put(e.getNoun().getValue(), e.getValue()));

        return expressions;
    }

    @Cacheable("multimapTransNoun")
    public Multimap getMultimapTranslationValue(){

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllNounTrans().forEach(
            e -> expressions.put(e.getValue(),e.getNoun().getValue()));

        return expressions;
    }

    public List<Noun> findByValue(String value) {
        return nounRepository.findByValue(this.stringBDD(value));
    }
    public List<NounTrans> findAllNounTrans() {
        return nounTransRepository.findAll();
    }

    public List<Noun> findNoun(Noun noun) {
        if (noun != null &&
            StringUtils.isNotBlank(noun.getValue()) &&
            StringUtils.isNotBlank(noun.getNumber()) &&
            StringUtils.isNotBlank(noun.getCompound()) &&
            StringUtils.isNotBlank(noun.getGender())) {
            return nounRepository.findByValueAndGenderAndNumberAndCompound(
                stringBDD(noun.getValue()), stringBDD(noun.getGender()),
                stringBDD(noun.getNumber()), stringBDD(noun.getCompound()));
        } else if (noun != null &&
                   StringUtils.isNotBlank(noun.getValue())) {
            return nounRepository.findByValue(noun.getValue());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Noun findOrCreate(Noun noun) {
        if (noun == null && StringUtils.isNotBlank(noun.getValue())) {
            log.error("Noun is null");
            throw new BadObjectException("Noun is invalide");
        }
        noun.lowerStrings();

        List<Noun> nounList = this.findNoun(noun);
        if (CollectionUtils.isEmpty(nounList)) {
            return nounRepository.save(noun);
        } else {
            log.info("Noun isn't findOrCreate because he already existe null");
            return nounList.iterator().next();
        }
    }
}
