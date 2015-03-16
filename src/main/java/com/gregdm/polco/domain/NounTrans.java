package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A NounTrans.
 */
@Entity
@Table(name = "T_NOUNTRANS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NounTrans implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    private Noun noun;

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

    public Noun getNoun() {
        return noun;
    }

    public void setNoun(Noun noun) {
        this.noun = noun;
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

        NounTrans nounTrans = (NounTrans) o;

        if (id != null ? !id.equals(nounTrans.id) : nounTrans.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "NounTrans{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
