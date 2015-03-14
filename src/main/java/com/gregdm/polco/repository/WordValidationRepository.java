package com.gregdm.polco.repository;

import com.gregdm.polco.domain.WordValidation;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WordValidation entity.
 */
public interface WordValidationRepository extends JpaRepository<WordValidation,Long> {

}
