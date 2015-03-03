package com.gregdm.polco.repository;

import com.gregdm.polco.domain.BadWord;
import com.gregdm.polco.domain.GoodWord;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Spring Data JPA repository for the BadWord entity.
 */
@Repository
public interface SearchBadWordRepository extends JpaRepository<BadWord,String>  {

    List<BadWord> findByValue(String value);

    List<BadWord> findByPrincipalAndType(String value, String type);
}
