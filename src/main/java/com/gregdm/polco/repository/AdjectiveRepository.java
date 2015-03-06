package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Adjective;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Adjective entity.
 */
public interface AdjectiveRepository extends JpaRepository<Adjective,Long> {
    List<Adjective> findByValue(String value);
}
