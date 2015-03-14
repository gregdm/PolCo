package com.gregdm.polco.service;

import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.domain.util.EnumWordType;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.NounRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.gregdm.polco.domain.util.EnumWordType.*;

@Service
@Transactional
public class WordValidationService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(WordValidationService.class);

    @Inject
    private NounService nounService;
    @Inject
    private AdjectiveService adjectiveService;
    @Inject
    private VerbService verbService;
    @Inject
    private PrefixService prefixService;
    @Inject
    private PrepositionService prepositionService;
    @Inject
    private NominalDetService nominalDetService;
    @Inject
    private InterjectionService interjectionService;
    @Inject
    private AdverbService adverbService;
    @Inject
    private ExpressionService expressionService;

    public boolean validate(WordValidation word) {
        if (word == null) {
            throw new BadObjectException("Nous ne pouvons pas valider le mot car il n'est pas conforme");
        }

        EnumWordType enumWordType = EnumWordType.valueOf(word.getWordType());
        if (enumWordType == null) {
            throw new BadObjectException("Nous ne pouvons pas valider le mot car il n'est pas conforme");
        }

        switch (enumWordType) {
            case NOUN:
                nounService.add(word);
                break;
            case ADJECTIVE:
                adjectiveService.add(word);
                break;
            case VERB:
                verbService.add(word);
                break;
            case INTERJECTION:
                interjectionService.add(word);
                break;
            case EXPRESSION:
                expressionService.add(word);
                break;
            case ADVERB:
                adverbService.add(word);
                break;
            default:
                return false;
        }
        return true;
    }
}
