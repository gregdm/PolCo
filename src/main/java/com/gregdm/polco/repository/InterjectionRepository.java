package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Interjection;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Interjection entity.
 */
public interface InterjectionRepository extends JpaRepository<Interjection,Long> {
    List<Interjection> findByValue(String value);
}
