package com.gregdm.polco.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.domain.ExpressionTrans;
import com.gregdm.polco.domain.WordValidation;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.ExpressionRepository;
import com.gregdm.polco.repository.ExpressionTransRepository;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

@Service
@Transactional
public class ExpressionService extends AbstractService {

    private final Logger log = LoggerFactory.getLogger(ExpressionService.class);

    @Inject
    private ExpressionRepository expressionRepository;

    @Inject
    private ExpressionTransRepository expressionTransRepository;

    public Multimap getMultimapTranslation(){

        Multimap<String, String> expressions = HashMultimap.create();

        //Handle duplicate key, multiple values
        this.findAllTrans().forEach(
            e -> expressions.put(e.getExpression().getValue(), e.getValue()));

        return expressions;
    }
    public boolean add(WordValidation word) {
        if (StringUtils.isBlank(word.getValue())) {
            return false;
        }
        Expression expression = new Expression();
        expression.setValue(word.getValue());
        expression = findOrCreate(expression);

        ExpressionTrans expressionTrans = new ExpressionTrans();
        expressionTrans.setValue(word.getTranslation());
        expressionTrans.setExpression(expression);
        if (CollectionUtils.isEmpty(expressionTransRepository
                                        .findByValueAndExpression(expressionTrans.getValue(),
                                                                  expressionTrans
                                                                      .getExpression()))) {
            expressionTrans.lowerStrings();
            expressionTransRepository.save(expressionTrans);
            return true;
        }
        return false;
    }

    public List<Expression> findByValue(String value) {
        return expressionRepository.findByValue(this.stringBDD(value));
    }

    public List<Expression> findAll() {
        return expressionRepository.findAll();
    }

    public List<ExpressionTrans> findAllTrans() {
        return expressionTransRepository.findAll();
    }

    public List<Expression> findExpression(Expression expression) {
        if (expression != null &&
            StringUtils.isNotBlank(expression.getValue())) {
            return expressionRepository.findByValue(
                stringBDD(expression.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Expression findOrCreate(Expression expression) {
        if (expression == null && StringUtils.isNotBlank(expression.getValue())) {
            log.error("Expression is null");
            throw new BadObjectException("Adective is invalide");
        }
        expression.lowerStrings();
//TODO GREG check en fonction de la traduction afin qu'une expression puisse avoir plusieurs tranduction

        List<Expression> expressionList = this.findExpression(expression);
        if (CollectionUtils.isEmpty(expressionList)) {
            return expressionRepository.save(expression);
        } else {
            log.info("Expression isn't findOrCreate because he already existe null");
            return expressionList.iterator().next();
        }
    }


}
