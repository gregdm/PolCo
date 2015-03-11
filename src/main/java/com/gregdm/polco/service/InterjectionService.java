package com.gregdm.polco.service;

import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.InterjectionRepository;
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
public class InterjectionService extends AbstractService{

    private final Logger log = LoggerFactory.getLogger(InterjectionService.class);

    @Inject
    private InterjectionRepository interjectionRepository;

    public List<Interjection> findByValue(String value){
        return interjectionRepository.findByValue(this.stringBDD(value));
    }

    public List<Interjection> findInterjection(Interjection adj){
        if(adj != null &&
            StringUtils.isNotBlank(adj.getValue())) {
            return interjectionRepository.findByValue(
                stringBDD(adj.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Interjection add(Interjection interjection){
        if(interjection == null && StringUtils.isNotBlank(interjection.getValue())){
            log.error("Interjection is null");
            throw new BadObjectException("Interjection is invalide");
        }
        interjection.lowerStrings();

        List<Interjection> InterjectionList = this.findInterjection(interjection);
        if(CollectionUtils.isEmpty(InterjectionList)) {
            return interjectionRepository.save(interjection);
        } else {
            log.info("Interjection isn't add because he already existe null");
            return InterjectionList.iterator().next();
        }
    }


}
