package com.gregdm.polco.service;

import com.gregdm.polco.domain.Preposition;
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
public class PrepositionService extends AbstractService{

    private final Logger log = LoggerFactory.getLogger(PrepositionService.class);

    @Inject
    private PrepositionRepository prepositionRepository;


    public List<Preposition> findByValue(String value){
        return prepositionRepository.findByValue(this.stringBDD(value));
    }

    public List<Preposition> findPreposition(Preposition prep){
        if(prep != null &&
            StringUtils.isNotBlank(prep.getValue())) {
            return prepositionRepository.findByValue(
                stringBDD(prep.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Preposition findOrCreate(Preposition prep){
        if(prep == null && StringUtils.isNotBlank(prep.getValue())){
            log.error("Preposition is null");
            throw new BadObjectException("Preposition is invalide");
        }
        prep.lowerStrings();

        List<Preposition> PrepositionList = this.findPreposition(prep);
        if(CollectionUtils.isEmpty(PrepositionList)) {
            return prepositionRepository.save(prep);
        } else {
            log.info("Preposition isn't findOrCreate because he already existe null");
            return PrepositionList.iterator().next();
        }
    }
}
