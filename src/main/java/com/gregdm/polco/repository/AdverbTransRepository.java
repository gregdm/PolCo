package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Adverb;
import com.gregdm.polco.domain.AdverbTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AdverbTrans entity.
 */
public interface AdverbTransRepository extends JpaRepository<AdverbTrans,Long> {
    List<AdverbTrans> findByValue(String value);
    List<AdverbTrans> findByValueAndAdverb(String value, Adverb adverb);
}
