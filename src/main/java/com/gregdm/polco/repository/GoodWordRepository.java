package com.gregdm.polco.repository;

import com.gregdm.polco.domain.GoodWord;
import com.gregdm.polco.domain.PersistentAuditEvent;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GoodWord entity.
 */
public interface GoodWordRepository extends JpaRepository<GoodWord,Long> {

}
