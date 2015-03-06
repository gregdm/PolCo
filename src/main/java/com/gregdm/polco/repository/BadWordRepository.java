package com.gregdm.polco.repository;

import com.gregdm.polco.domain.BadWord;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BadWord entity.
 */
public interface BadWordRepository extends JpaRepository<BadWord,Long> {
    List<BadWord> findByValue(String value);

}
