package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AdjectiveTrans.
 */
@Entity
@Table(name = "T_ADJECTIVETRANS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdjectiveTrans extends AbstractWord implements Serializable {

    @ManyToOne
    private Adjective adjective;

    public Adjective getAdjective() {
        return adjective;
    }

    public void setAdjective(Adjective adjective) {
        this.adjective = adjective;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdjectiveTrans adjectiveTrans = (AdjectiveTrans) o;

        if (id != null ? !id.equals(adjectiveTrans.id) : adjectiveTrans.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AdjectiveTrans{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
