package com.gregdm.polco.repository;

import com.gregdm.polco.domain.NominalDet;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NominalDet entity.
 */
public interface NominalDetRepository extends JpaRepository<NominalDet,Long> {

}
