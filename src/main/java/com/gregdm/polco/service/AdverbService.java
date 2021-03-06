package com.gregdm.polco.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.domain.InterjectionTrans;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.AdverbRepository;
import com.gregdm.polco.repository.AdverbTransRepository;

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
public class AdverbService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(AdverbService.class);

    @Inject
    private AdverbRepository adverbRepository;
    @Inject
    private AdverbTransRepository adverbTransRepository;

    public List<AdverbTrans> findAllAdverbTrans() {
        return adverbTransRepository.findAll();
    }


    @CacheEvict(value = { "multimapAdverbTrans", "multimapTransAdverb" }, allEntries = true)
    public boolean add(WordValidation word) {
        if (StringUtils.isBlank(word.getValue())) {
            return false;
        }
        Adverb adverb = new Adverb();
        adverb.setValue(word.getValue());
        adverb = findOrCreate(adverb);

        AdverbTrans adverbTrans = new AdverbTrans();
        adverbTrans.setValue(word.getTranslation());
        adverbTrans.setAdverb(adverb);
        if (CollectionUtils.isEmpty(adverbTransRepository
                                        .findByValueAndAdverb(adverbTrans.getValue(),
                                                              adverbTrans.getAdverb()))) {
            adverbTrans.lowerStrings();
            adverbTransRepository.save(adverbTrans);
            return true;
        }
        return false;
    }

    @Cacheable("multimapAdverbTrans")
    public Multimap<String, String> getMultimapTranslation() {

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllAdverbTrans().forEach(
            e -> expressions.put(e.getAdverb().getValue(),e.getValue()));

        return expressions;
    }

    @Cacheable("multimapTransAdverb")
    public Multimap<String, String> getMultimapTranslationValue() {

        Multimap<String, String> expressions = HashMultimap.create();

        this.findAllAdverbTrans().forEach(
            e -> expressions.put(e.getValue(),e.getAdverb().getValue()));

        return expressions;
    }


    public List<Adverb> findByValue(String value) {
        return adverbRepository.findByValue(this.stringBDD(value));
    }

    public List<Adverb> findAdverb(Adverb adj) {
        if (adj != null &&
            StringUtils.isNotBlank(adj.getValue())) {
            return adverbRepository.findByValue(
                stringBDD(adj.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Adverb findOrCreate(Adverb adverb) {
        if (adverb == null && StringUtils.isNotBlank(adverb.getValue())) {
            log.error("Adverb is null");
            throw new BadObjectException("Adective is invalide");
        }
        adverb.lowerStrings();

        List<Adverb> AdverbList = this.findAdverb(adverb);
        if (CollectionUtils.isEmpty(AdverbList)) {
            return adverbRepository.save(adverb);
        } else {
            log.info("Adverb isn't findOrCreate because he already existe null");
            return AdverbList.iterator().next();
        }
    }


}
