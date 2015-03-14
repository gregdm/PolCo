package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Noun;
import com.gregdm.polco.domain.NounTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NounTrans entity.
 */
public interface NounTransRepository extends JpaRepository<NounTrans,Long> {

    List<NounTrans> findByValue(String value);
    List<NounTrans> findByValueAndNoun(String value, Noun noun);
}
