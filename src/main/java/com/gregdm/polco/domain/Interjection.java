package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Interjection.
 */
@Entity
@Table(name = "T_INTERJECTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Interjection extends AbstractWord implements Serializable {


    @OneToMany(mappedBy = "interjection")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InterjectionTrans> translationss = new HashSet<>();


    public Set<InterjectionTrans> getTranslationss() {
        return translationss;
    }

    public void setTranslationss(Set<InterjectionTrans> interjectionTranss) {
        this.translationss = interjectionTranss;
    }

    public void lowerStrings(){
        this.setValue(this.getValue().toLowerCase().trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Interjection interjection = (Interjection) o;

        if (id != null ? !id.equals(interjection.id) : interjection.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Interjection{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
