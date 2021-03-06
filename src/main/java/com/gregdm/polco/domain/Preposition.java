package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Preposition.
 */
@Entity
@Table(name = "T_PREPOSITION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Preposition extends AbstractWord implements Serializable {

    @OneToMany(mappedBy = "preposition")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PrepositionTrans> translationss = new HashSet<>();

    public Set<PrepositionTrans> getTranslationss() {
        return translationss;
    }

    public void setTranslationss(Set<PrepositionTrans> prepositionTranss) {
        this.translationss = prepositionTranss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Preposition preposition = (Preposition) o;

        if (id != null ? !id.equals(preposition.id) : preposition.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Preposition{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
