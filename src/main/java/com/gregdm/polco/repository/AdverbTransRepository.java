package com.gregdm.polco.repository;

import com.gregdm.polco.domain.AdverbTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AdverbTrans entity.
 */
public interface AdverbTransRepository extends JpaRepository<AdverbTrans,Long> {

}
