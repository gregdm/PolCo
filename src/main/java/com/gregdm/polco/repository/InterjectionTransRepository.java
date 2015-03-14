package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Interjection;
import com.gregdm.polco.domain.InterjectionTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InterjectionTrans entity.
 */
public interface InterjectionTransRepository extends JpaRepository<InterjectionTrans,Long> {
    List<InterjectionTrans> findByValue(String value);
    List<InterjectionTrans> findByValueAndInterjection(String value, Interjection interjection);
}
