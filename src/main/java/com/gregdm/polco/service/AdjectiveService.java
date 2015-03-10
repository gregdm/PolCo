package com.gregdm.polco.service;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class AdjectiveService extends AbstractService{

    private final Logger log = LoggerFactory.getLogger(AdjectiveService.class);

    @Inject
    private AdjectiveRepository adjectiveRepository;

    public List<Adjective> findByValue(String value){
        return adjectiveRepository.findByValue(this.stringBDD(value));
    }

    public List<Adjective> findAdjective(Adjective adj){
        if(adj != null &&
            StringUtils.isNotBlank(adj.getValue())&&
            StringUtils.isNotBlank(adj.getNumber())&&
            StringUtils.isNotBlank(adj.getGender())) {
            return adjectiveRepository.findByValueAndGenderAndNumber(
                stringBDD(adj.getValue()), stringBDD(adj.getGender()),
                stringBDD(adj.getNumber()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Adjective add(Adjective adj){
        if(adj == null && StringUtils.isNotBlank(adj.getValue())){
            log.error("Adjective is null");
            throw new BadObjectException("Adective is invalide");
        }
        adj.lowerStrings();

        List<Adjective> adjectiveList = this.findAdjective(adj);
        if(CollectionUtils.isEmpty(adjectiveList)) {
            return adjectiveRepository.save(adj);
        } else {
            log.info("Adjective isn't add because he already existe null");
            return adjectiveList.iterator().next();
        }
    }
}
