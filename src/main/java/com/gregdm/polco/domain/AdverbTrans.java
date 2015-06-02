package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AdverbTrans.
 */
@Entity
@Table(name = "T_ADVERBTRANS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdverbTrans extends AbstractWord implements Serializable {


    @ManyToOne
    private Adverb adverb;

    public Adverb getAdverb() {
        return adverb;
    }

    public void setAdverb(Adverb adverb) {
        this.adverb = adverb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdverbTrans adverbTrans = (AdverbTrans) o;

        if (id != null ? !id.equals(adverbTrans.id) : adverbTrans.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "AdverbTrans{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
