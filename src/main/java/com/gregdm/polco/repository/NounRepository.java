package com.gregdm.polco.repository;

import com.gregdm.polco.domain.Noun;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Noun entity.
 */
public interface NounRepository extends JpaRepository<Noun,Long> {

    List<Noun> findByValue(String value);

    List<Noun> findByValueAndGenderAndNumberAndCompound(String value, String gender, String number, String compound);

    List<Noun> findByValueAndGenderAndNumber(String value, String gender, String number);

}
