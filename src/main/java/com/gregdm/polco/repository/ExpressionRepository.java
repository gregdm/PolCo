package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Expression;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Expression entity.
 */
public interface ExpressionRepository extends JpaRepository<Expression,Long> {
    List<Expression> findByValue(String value);
}
