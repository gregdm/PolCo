package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Adverb.
 */
@Entity
@Table(name = "T_ADVERB")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Adverb extends AbstractWord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private String value;

    @OneToMany(mappedBy = "adverb")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AdverbTrans> translationss = new HashSet<>();

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

    public Set<AdverbTrans> getTranslationss() {
        return translationss;
    }

    public void setTranslationss(Set<AdverbTrans> adverbTranss) {
        this.translationss = adverbTranss;
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

        Adverb adverb = (Adverb) o;

        if (id != null ? !id.equals(adverb.id) : adverb.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Adverb{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
