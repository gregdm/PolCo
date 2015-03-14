package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Adjective;
import com.gregdm.polco.domain.AdjectiveTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AdjectiveTrans entity.
 */
public interface AdjectiveTransRepository extends JpaRepository<AdjectiveTrans,Long> {

    List<AdjectiveTrans> findByValue(String value);
    List<Adjective> findByValueAndAdjective(String value, Adjective adjective);
}
