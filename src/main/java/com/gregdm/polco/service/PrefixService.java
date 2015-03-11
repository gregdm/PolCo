package com.gregdm.polco.service;

import com.gregdm.polco.domain.Prefix;
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
public class PrefixService extends AbstractService{

    private final Logger log = LoggerFactory.getLogger(PrefixService.class);

    @Inject
    private PrefixRepository prefixRepository;

    public List<Prefix> findByValue(String value){
        return prefixRepository.findByValue(this.stringBDD(value));
    }

    public List<Prefix> findPrefix(Prefix adj){
        if(adj != null &&
            StringUtils.isNotBlank(adj.getValue())) {
            return prefixRepository.findByValue(
                stringBDD(adj.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Prefix add(Prefix prefix){
        if(prefix == null && StringUtils.isNotBlank(prefix.getValue())){
            log.error("Prefix is null");
            throw new BadObjectException("Prefix is invalide");
        }
        prefix.lowerStrings();

        List<Prefix> prefixList = this.findPrefix(prefix);
        if(CollectionUtils.isEmpty(prefixList)) {
            return prefixRepository.save(prefix);
        } else {
            log.info("Prefix isn't add because he already existe null");
            return prefixList.iterator().next();
        }
    }

}
