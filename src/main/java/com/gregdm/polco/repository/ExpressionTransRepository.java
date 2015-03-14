package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.domain.AdverbTrans;
import com.gregdm.polco.domain.Expression;
import com.gregdm.polco.domain.ExpressionTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExpressionTrans entity.
 */
public interface ExpressionTransRepository extends JpaRepository<ExpressionTrans,Long> {
    List<ExpressionTrans> findByValue(String value);
    List<ExpressionTrans> findByValueAndExpression(String value, Expression expression);
}
