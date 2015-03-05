package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Adverb;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Adverb entity.
 */
public interface AdverbRepository extends JpaRepository<Adverb,Long> {

}
