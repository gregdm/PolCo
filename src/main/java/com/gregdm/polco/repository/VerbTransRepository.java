package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Verb;
import com.gregdm.polco.domain.VerbTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VerbTrans entity.
 */
public interface VerbTransRepository extends JpaRepository<VerbTrans,Long> {
    List<VerbTrans> findByValue(String value);
    List<VerbTrans> findByValueAndVerb(String value, Verb verb);

}
