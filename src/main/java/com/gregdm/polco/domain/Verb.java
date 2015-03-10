package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Verb.
 */
@Entity
@Table(name = "T_VERB")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Verb implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "tense")
    private String tense;

    @Column(name = "person")
    private String person;

    @Column(name = "number")
    private String number;

    @OneToMany(mappedBy = "verb")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VerbTrans> translationss = new HashSet<>();

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

    public String getTense() {
        return tense;
    }

    public void setTense(String tense) {
        this.tense = tense;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<VerbTrans> getTranslationss() {
        return translationss;
    }

    public void setTranslationss(Set<VerbTrans> verbTranss) {
        this.translationss = verbTranss;
    }

    public void lowerStrings(){
        this.setValue(this.getValue().toLowerCase().trim());
        this.setTense(this.getTense().toLowerCase().trim());
        this.setPerson(this.getPerson().toLowerCase().trim());
        this.setNumber(this.getNumber().toLowerCase().trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Verb verb = (Verb) o;

        if (id != null ? !id.equals(verb.id) : verb.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Verb{" +
                "id=" + id +
                ", value='" + value + "'" +
                ", tense='" + tense + "'" +
                ", person='" + person + "'" +
                ", number='" + number + "'" +
                '}';
    }
}
