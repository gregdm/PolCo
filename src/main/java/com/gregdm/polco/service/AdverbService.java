package com.gregdm.polco.service;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.domain.Adverb;
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
public class AdverbService extends AbstractService{

    private final Logger log = LoggerFactory.getLogger(AdverbService.class);

    @Inject
    private AdverbRepository adverbRepository;

    public List<Adverb> findByValue(String value){
        return adverbRepository.findByValue(this.stringBDD(value));
    }

    public List<Adverb> findAdverb(Adverb adj){
        if(adj != null &&
            StringUtils.isNotBlank(adj.getValue())) {
            return adverbRepository.findByValue(
                stringBDD(adj.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Adverb add(Adverb adverb){
        if(adverb == null && StringUtils.isNotBlank(adverb.getValue())){
            log.error("Adverb is null");
            throw new BadObjectException("Adective is invalide");
        }
        adverb.lowerStrings();

        List<Adverb> AdverbList = this.findAdverb(adverb);
        if(CollectionUtils.isEmpty(AdverbList)) {
            return adverbRepository.save(adverb);
        } else {
            log.info("Adverb isn't add because he already existe null");
            return AdverbList.iterator().next();
        }
    }


}
