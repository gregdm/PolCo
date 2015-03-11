package com.gregdm.polco.repository;

import com.gregdm.polco.domain.ExpressionTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExpressionTrans entity.
 */
public interface ExpressionTransRepository extends JpaRepository<ExpressionTrans,Long> {

}
