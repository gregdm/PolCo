package com.gregdm.polco.service;

import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.domain.util.EnumWordType;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.WordValidationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class WordValidationService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(WordValidationService.class);

    @Inject
    private WordValidationRepository wordValidationRepository;
    @Inject
    private NounService nounService;
    @Inject
    private AdjectiveService adjectiveService;
    @Inject
    private VerbService verbService;
    @Inject
    private InterjectionService interjectionService;
    @Inject
    private AdverbService adverbService;
    @Inject
    private ExpressionService expressionService;

    public boolean validate(WordValidation word) {
        if (word == null) {
            throw new BadObjectException(
                "Nous ne pouvons pas valider le mot car il n'est pas conforme");
        }

        EnumWordType enumWordType = EnumWordType.valueOf(word.getWordType());
        if (enumWordType == null) {
            throw new BadObjectException(
                "Nous ne pouvons pas valider le mot car il n'est pas conforme");
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
        wordValidationRepository.delete(word);
        return true;
    }
}
