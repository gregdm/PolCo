package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A NominalDet.
 */
@Entity
@Table(name = "T_NOMINALDET")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NominalDet extends AbstractWord implements Serializable {

    @OneToMany(mappedBy = "nominalDet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NominalDetTrans> translationss = new HashSet<>();

    public Set<NominalDetTrans> getTranslationss() {
        return translationss;
    }

    public void setTranslationss(Set<NominalDetTrans> nominalDetTranss) {
        this.translationss = nominalDetTranss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NominalDet nominalDet = (NominalDet) o;

        if (id != null ? !id.equals(nominalDet.id) : nominalDet.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "NominalDet{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
