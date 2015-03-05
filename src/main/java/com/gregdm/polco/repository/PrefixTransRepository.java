package com.gregdm.polco.repository;

import com.gregdm.polco.domain.PrefixTrans;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PrefixTrans entity.
 */
public interface PrefixTransRepository extends JpaRepository<PrefixTrans,Long> {

}
