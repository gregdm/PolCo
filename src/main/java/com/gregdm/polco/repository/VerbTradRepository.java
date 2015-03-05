package com.gregdm.polco.repository;

import com.gregdm.polco.domain.VerbTrad;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the VerbTrad entity.
 */
public interface VerbTradRepository extends JpaRepository<VerbTrad,Long> {

}
