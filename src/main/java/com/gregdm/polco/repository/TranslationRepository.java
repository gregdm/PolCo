package com.gregdm.polco.repository;

import com.gregdm.polco.domain.BadWord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the BadWord entity.
 */
public interface TranslationRepository extends JpaRepository<BadWord,Long> {

}
