package com.gregdm.polco.repository;

import com.gregdm.polco.domain.NominalDetTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NominalDetTrans entity.
 */
public interface NominalDetTransRepository extends JpaRepository<NominalDetTrans,Long> {
    List<NominalDetTrans> findByValue(String value);
}
