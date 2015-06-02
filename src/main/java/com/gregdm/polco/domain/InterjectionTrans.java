package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A InterjectionTrans.
 */
@Entity
@Table(name = "T_INTERJECTIONTRANS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InterjectionTrans extends AbstractWord implements Serializable {

    @ManyToOne
    private Interjection interjection;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Interjection getInterjection() {
        return interjection;
    }

    public void setInterjection(Interjection interjection) {
        this.interjection = interjection;
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

        InterjectionTrans interjectionTrans = (InterjectionTrans) o;

        if (id != null ? !id.equals(interjectionTrans.id) : interjectionTrans.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "InterjectionTrans{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
