package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Prefix.
 */
@Entity
@Table(name = "T_PREFIX")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Prefix extends AbstractWord implements Serializable {

    @OneToMany(mappedBy = "prefix")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PrefixTrans> translationss = new HashSet<>();

    public Set<PrefixTrans> getTranslationss() {
        return translationss;
    }

    public void setTranslationss(Set<PrefixTrans> prefixTranss) {
        this.translationss = prefixTranss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Prefix prefix = (Prefix) o;

        if (id != null ? !id.equals(prefix.id) : prefix.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Prefix{" +
                "id=" + id +
                ", value='" + value + "'" +
                '}';
    }
}
