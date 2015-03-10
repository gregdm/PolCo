package com.gregdm.polco.service;

import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.domain.Verb;
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
public class VerbService extends  AbstractService{

    private final Logger log = LoggerFactory.getLogger(VerbService.class);

    @Inject
    private VerbRepository verbRepository;

    public List<Verb> findByValue(String value){
        return verbRepository.findByValue(this.stringBDD(value));
    }

    public List<Verb> findVerb(Verb verb){
        if(verb != null &&
            StringUtils.isNotBlank(verb.getValue())&&
            StringUtils.isNotBlank(verb.getNumber())&&
            StringUtils.isNotBlank(verb.getTense())&&
            StringUtils.isNotBlank(verb.getPerson())) {
            return verbRepository.findByValueAndPersonAndTenseAndNumber(
                stringBDD(verb.getValue()), stringBDD(verb.getPerson()),
                stringBDD(verb.getTense()), stringBDD(verb.getNumber()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Verb add(Verb verb){
        if(verb == null && StringUtils.isNotBlank(verb.getValue())){
            log.error("Verb is null");
            throw new BadObjectException("Verb is invalide");
        }
        verb.lowerStrings();

        List<Verb> VerbList = this.findVerb(verb);
        if(CollectionUtils.isEmpty(VerbList)) {
            return verbRepository.save(verb);
        } else {
            log.info("Verb isn't add because he already existe null");
            return VerbList.iterator().next();
        }
    }


}
