package com.gregdm.polco.service;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.domain.AdjectiveTrans;
import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.domain.ExpressionTrans;
import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.domain.InterjectionTrans;
import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.domain.NounTrans;
import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.domain.VerbTrans;
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

    public WordValidation expressionTransToWordValidation(ExpressionTrans e){
        WordValidation w = new WordValidation();
        w.setTranslation(e.getValue());
        w.setValue(e.getExpression().getValue());
        w.setWordType(EnumWordType.EXPRESSION.name());
        return w;
    }
    public WordValidation nounTransToWordValidation(NounTrans n){
        WordValidation w = new WordValidation();
        w.setTranslation(n.getValue());
        Noun noun = n.getNoun();
        w.setValue(noun.getValue());
        w.setWordType(EnumWordType.NOUN.name());
        w.setGender(noun.getGender());
        w.setNumber(noun.getNumber());
        return w;
    }

    public WordValidation adjectiveTransToWordValidation(AdjectiveTrans a){
        WordValidation w = new WordValidation();
        w.setTranslation(a.getValue());
        Adjective adj = a.getAdjective();
        w.setValue(adj.getValue());
        w.setWordType(EnumWordType.ADJECTIVE.name());
        w.setGender(adj.getGender());
        w.setNumber(adj.getNumber());
        return w;
    }
    public WordValidation verbTransToWordValidation(VerbTrans v){
        WordValidation w = new WordValidation();
        w.setTranslation(v.getValue());
        Verb verb = v.getVerb();
        w.setValue(verb.getValue());
        w.setWordType(EnumWordType.VERB.name());
        w.setNumber(verb.getNumber());
        w.setPerson(verb.getPerson());
        w.setTense(verb.getTense());
        return w;
    }
    public WordValidation interjectionTransToWordValidation(InterjectionTrans i){
        WordValidation w = new WordValidation();
        w.setTranslation(i.getValue());
        Interjection interj = i.getInterjection();
        w.setValue(interj.getValue());
        w.setWordType(EnumWordType.INTERJECTION.name());
        return w;
    }

    public WordValidation adverbTransToWordValidation(AdverbTrans a){
        WordValidation w = new WordValidation();
        w.setTranslation(a.getValue());
        Adverb adverb = a.getAdverb();
        w.setValue(adverb.getValue());
        w.setWordType(EnumWordType.ADVERB.name());
        return w;
    }

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
