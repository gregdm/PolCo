package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Noun.
 */
@Entity
@Table(name = "T_NOUN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Noun extends AbstractWord implements Serializable {

    @Column(name = "gender")
    private String gender;

    @Column(name = "number")
    private String number;

    @Column(name = "compound")
    private String compound;

    @OneToMany(mappedBy = "noun")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NounTrans> translationss = new HashSet<>();

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

    public String getCompound() {
        return compound;
    }

    public void setCompound(String compound) {
        this.compound = compound;
    }

    public Set<NounTrans> getTranslationss() {
        return translationss;
    }

    public void lowerStrings(){
        this.setValue(this.getValue().toLowerCase().trim());
        this.setCompound(this.getCompound().toLowerCase().trim());
        this.setGender(this.getGender().toLowerCase().trim());
        this.setNumber(this.getNumber().toLowerCase().trim());
    }

    public void setTranslationss(Set<NounTrans> nounTranss) {
        this.translationss = nounTranss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Noun noun = (Noun) o;

        if (id != null ? !id.equals(noun.id) : noun.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Noun{" +
                "id=" + id +
                ", value='" + value + "'" +
                ", gender='" + gender + "'" +
                ", number='" + number + "'" +
                ", compound='" + compound + "'" +
                '}';
    }
}
