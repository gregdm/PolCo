package com.gregdm.polco.service;

import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.exception.BadObjectException;
import com.gregdm.polco.repository.ExpressionRepository;
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
public class ExpressionService extends AbstractService{

    private final Logger log = LoggerFactory.getLogger(ExpressionService.class);

    @Inject
    private ExpressionRepository expressionRepository;

    public List<Expression> findByValue(String value){
        return expressionRepository.findByValue(this.stringBDD(value));
    }

    public List<Expression> findAll(){
        return expressionRepository.findAll();
    }

    public List<Expression> findExpression(Expression expression){
        if(expression != null &&
            StringUtils.isNotBlank(expression.getValue())) {
            return expressionRepository.findByValue(
                stringBDD(expression.getValue()));
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    public Expression add(Expression expression){
        if(expression == null && StringUtils.isNotBlank(expression.getValue())){
            log.error("Expression is null");
            throw new BadObjectException("Adective is invalide");
        }
        expression.lowerStrings();

        List<Expression> expressionList = this.findExpression(expression);
        if(CollectionUtils.isEmpty(expressionList)) {
            return expressionRepository.save(expression);
        } else {
            log.info("Expression isn't add because he already existe null");
            return expressionList.iterator().next();
        }
    }


}
