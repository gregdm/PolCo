package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Adjective.
 */
@Entity
@Table(name = "T_ADJECTIVE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Adjective extends AbstractWord implements Serializable {

    @Column(name = "gender")
    private String gender;

    @Column(name = "number")
    private String number;

    @OneToMany(mappedBy = "adjective")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AdjectiveTrans> translationss = new HashSet<>();

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<AdjectiveTrans> getTranslationss() {
        return translationss;
    }

    public void setTranslationss(Set<AdjectiveTrans> adjectiveTranss) {
        this.translationss = adjectiveTranss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Adjective adjective = (Adjective) o;

        if (id != null ? !id.equals(adjective.id) : adjective.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Adjective{" +
                "id=" + id +
                ", value='" + value + "'" +
                ", gender='" + gender + "'" +
                ", number='" + number + "'" +
                '}';
    }

    public void lowerStrings() {
        this.setValue(this.getValue().toLowerCase().trim());
        this.setGender(this.getGender().toLowerCase().trim());
        this.setNumber(this.getNumber().toLowerCase().trim());
    }
}
