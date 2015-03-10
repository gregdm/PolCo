package com.gregdm.polco.service;

import com.gregdm.polco.domain.Noun;
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
public class NounService extends AbstractService{

    private final Logger log = LoggerFactory.getLogger(NounService.class);

    @Inject
    private NounRepository nounRepository;

    public List<Noun> findByValue(String value){
        return nounRepository.findByValue(this.stringBDD(value));
    }

    public List<Noun> findNoun(Noun noun){
        if(noun != null &&
            StringUtils.isNotBlank(noun.getValue())&&
            StringUtils.isNotBlank(noun.getNumber())&&
            StringUtils.isNotBlank(noun.getCompound())&&
            StringUtils.isNotBlank(noun.getGender())) {
            return nounRepository.findByValueAndGenderAndNumberAndCompound(
                stringBDD(noun.getValue()), stringBDD(noun.getGender()),
                stringBDD(noun.getNumber()), stringBDD(noun.getCompound()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Noun add(Noun noun){
        if(noun == null && StringUtils.isNotBlank(noun.getValue())){
            log.error("Noun is null");
            throw new BadObjectException("Noun is invalide");
        }
        noun.lowerStrings();

        List<Noun> nounList = this.findNoun(noun);
        if(CollectionUtils.isEmpty(nounList)) {
            return nounRepository.save(noun);
        } else {
            log.info("Noun isn't add because he already existe null");
           return nounList.iterator().next();
        }
    }


}
