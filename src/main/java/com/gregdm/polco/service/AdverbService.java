package com.gregdm.polco.service;

import com.gregdm.polco.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class AdverbService {

    private final Logger log = LoggerFactory.getLogger(AdverbService.class);

    @Inject
    private AdverbRepository adverbRepository;

}
