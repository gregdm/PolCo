package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Preposition;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Preposition entity.
 */
public interface PrepositionRepository extends JpaRepository<Preposition,Long> {

}
