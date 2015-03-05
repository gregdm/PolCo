package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Prefix;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prefix entity.
 */
public interface PrefixRepository extends JpaRepository<Prefix,Long> {

}
