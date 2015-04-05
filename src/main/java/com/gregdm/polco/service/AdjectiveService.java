package com.gregdm.polco.service;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.AdjectiveRepository;
import com.gregdm.polco.repository.AdjectiveTransRepository;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

@Service
@Transactional
public class AdjectiveService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(AdjectiveService.class);

    @Inject
    private AdjectiveRepository adjectiveRepository;
    @Inject
    private AdjectiveTransRepository adjectiveTransRepository;

    public List<AdjectiveTrans> findAllAdjectiveTrans() {
        return adjectiveTransRepository.findAll();
    }

    public boolean add(WordValidation word) {
        if (StringUtils.isBlank(word.getValue())) {
            return false;
        }
        Adjective adjective = new Adjective();
        adjective.setValue(word.getValue());
        adjective.setGender(word.getGender());
        adjective.setNumber(word.getNumber());

        adjective = findOrCreate(adjective);

        AdjectiveTrans adjectiveTrans = new AdjectiveTrans();
        adjectiveTrans.setValue(word.getTranslation());
        adjectiveTrans.setAdjective(adjective);
        if (CollectionUtils.isEmpty(adjectiveTransRepository
                                        .findByValueAndAdjective(adjectiveTrans.getValue(),
                                                                 adjectiveTrans.getAdjective()))) {
            adjectiveTrans.lowerStrings();
            adjectiveTransRepository.save(adjectiveTrans);
            return true;
        }
        return false;
    }

    public List<Adjective> findByValue(String value) {
        return adjectiveRepository.findByValue(this.stringBDD(value));
    }

    public List<Adjective> findAdjective(Adjective adj) {
        if (adj != null &&
            StringUtils.isNotBlank(adj.getValue()) &&
            StringUtils.isNotBlank(adj.getNumber()) &&
            StringUtils.isNotBlank(adj.getGender())) {
            return adjectiveRepository.findByValueAndGenderAndNumber(
                stringBDD(adj.getValue()), stringBDD(adj.getGender()),
                stringBDD(adj.getNumber()));
        } else if (adj != null &&
                   StringUtils.isNotBlank(adj.getValue())) {
            return adjectiveRepository.findByValue(adj.getValue());
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Adjective findOrCreate(Adjective adj) {
        if (adj == null && StringUtils.isNotBlank(adj.getValue())) {
            log.error("Adjective is null");
            throw new BadObjectException("Adective is invalide");
        }
        adj.lowerStrings();

        List<Adjective> adjectiveList = this.findAdjective(adj);
        if (CollectionUtils.isEmpty(adjectiveList)) {
            return adjectiveRepository.save(adj);
        } else {
            log.info("Adjective isn't findOrCreate because he already existe null");
            return adjectiveList.iterator().next();
        }
    }
}
