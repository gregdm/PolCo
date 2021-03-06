package com.gregdm.polco.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.domain.InterjectionTrans;
import com.gregdm.polco.domain.VerbTrans;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.InterjectionRepository;
import com.gregdm.polco.repository.InterjectionTransRepository;

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
public class InterjectionService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(InterjectionService.class);

    @Inject
    private InterjectionRepository interjectionRepository;
    @Inject
    private InterjectionTransRepository interjectionTransRepository;

    public List<InterjectionTrans> findAllInterjectionTrans() {
        return interjectionTransRepository.findAll();
    }

    @CacheEvict(value = { "multimapTransInterjection", "multimapInterjectionTrans" }, allEntries = true)
    public boolean add(WordValidation word) {
        if (StringUtils.isBlank(word.getValue())) {
            return false;
        }
        Interjection interjection = new Interjection();
        interjection.setValue(word.getValue());
        interjection = findOrCreate(interjection);

        InterjectionTrans interjectionTrans = new InterjectionTrans();
        interjectionTrans.setValue(word.getTranslation());
        interjectionTrans.setInterjection(interjection);
        if (CollectionUtils.isEmpty(interjectionTransRepository
                                        .findByValueAndInterjection(interjectionTrans.getValue(),
                                                                    interjectionTrans
                                                                        .getInterjection()))) {
            interjectionTrans.lowerStrings();
            interjectionTransRepository.save(interjectionTrans);
            return true;
        }
        return false;
    }


    @Cacheable("multimapInterjectionTrans")
    public Multimap getMultimapTranslation() {

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllInterjectionTrans().forEach(
            e -> expressions.put(e.getInterjection().getValue(),e.getValue()));

        return expressions;
    }

    @Cacheable("multimapTransInterjection")
    public Multimap getMultimapTranslationValue() {

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllInterjectionTrans().forEach(
            e -> expressions.put(e.getValue(),e.getInterjection().getValue()));

        return expressions;
    }

    public List<Interjection> findByValue(String value) {
        return interjectionRepository.findByValue(this.stringBDD(value));
    }

    public List<Interjection> findInterjection(Interjection adj) {
        if (adj != null &&
            StringUtils.isNotBlank(adj.getValue())) {
            return interjectionRepository.findByValue(
                stringBDD(adj.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Interjection findOrCreate(Interjection interjection) {
        if (interjection == null && StringUtils.isNotBlank(interjection.getValue())) {
            log.error("Interjection is null");
            throw new BadObjectException("Interjection is invalide");
        }
        interjection.lowerStrings();

        List<Interjection> InterjectionList = this.findInterjection(interjection);
        if (CollectionUtils.isEmpty(InterjectionList)) {
            return interjectionRepository.save(interjection);
        } else {
            log.info("Interjection isn't findOrCreate because he already existe null");
            return InterjectionList.iterator().next();
        }
    }


}
