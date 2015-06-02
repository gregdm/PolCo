package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PrepositionTrans.
 */
@Entity
@Table(name = "T_PREPOSITIONTRANS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrepositionTrans extends AbstractWord implements Serializable {

    @ManyToOne
    private Preposition preposition;

    public Preposition getPreposition() {
        return preposition;
    }

    public void setPreposition(Preposition preposition) {
        this.preposition = preposition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrepositionTrans prepositionTrans = (PrepositionTrans) o;

        if (id != null ? !id.equals(prepositionTrans.id) : prepositionTrans.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "PrepositionTrans{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
