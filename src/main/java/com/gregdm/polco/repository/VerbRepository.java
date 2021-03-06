package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Verb;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Verb entity.
 */
public interface VerbRepository extends JpaRepository<Verb,Long> {
    List<Verb> findByValue(String value);

    List<Verb> findByValueAndPersonAndTenseAndNumber(String value, String person, String tense, String number);

}
