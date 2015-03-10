package com.gregdm.polco.service;

import com.gregdm.polco.repository.InterjectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class InterjectionService {

    private final Logger log = LoggerFactory.getLogger(InterjectionService.class);

    @Inject
    private InterjectionRepository interjectionRepository;
}
