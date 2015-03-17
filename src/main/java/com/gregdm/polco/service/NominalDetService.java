package com.gregdm.polco.service;

import com.gregdm.polco.domain.NominalDet;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.NominalDetRepository;

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
public class NominalDetService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(NominalDetService.class);


    @Inject
    private NominalDetRepository nominalDetRepository;


    public List<NominalDet> findByValue(String value) {
        return nominalDetRepository.findByValue(this.stringBDD(value));
    }

    public List<NominalDet> findNominalDet(NominalDet adj) {
        if (adj != null &&
            StringUtils.isNotBlank(adj.getValue())) {
            return nominalDetRepository.findByValue(
                stringBDD(adj.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public NominalDet findOrCreate(NominalDet nominalDet) {
        if (nominalDet == null && StringUtils.isNotBlank(nominalDet.getValue())) {
            log.error("NominalDet is null");
            throw new BadObjectException("NominalDet is invalide");
        }
        nominalDet.lowerStrings();

        List<NominalDet> nominalDetList = this.findNominalDet(nominalDet);
        if (CollectionUtils.isEmpty(nominalDetList)) {
            return nominalDetRepository.save(nominalDet);
        } else {
            log.info("NominalDet isn't findOrCreate because he already existe null");
            return nominalDetList.iterator().next();
        }
    }
}
