package com.gregdm.polco.repository;

import com.gregdm.polco.domain.BadWord;
import com.gregdm.polco.domain.GoodWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the BadWord entity.
 */
@Repository
public interface SearchGoodWordRepository extends JpaRepository<GoodWord,String>  {

    List<GoodWord> findByValue(String value);

    List<GoodWord> findByPrincipalAndType(String value, String level);
}
