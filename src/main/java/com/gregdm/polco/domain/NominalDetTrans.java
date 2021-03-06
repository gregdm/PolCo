package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A NominalDetTrans.
 */
@Entity
@Table(name = "T_NOMINALDETTRANS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NominalDetTrans extends AbstractWord implements Serializable {

    @ManyToOne
    private NominalDet nominalDet;

    public NominalDet getNominalDet() {
        return nominalDet;
    }

    public void setNominalDet(NominalDet nominalDet) {
        this.nominalDet = nominalDet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NominalDetTrans nominalDetTrans = (NominalDetTrans) o;

        if (id != null ? !id.equals(nominalDetTrans.id) : nominalDetTrans.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "NominalDetTrans{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
