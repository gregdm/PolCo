package com.gregdm.polco.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Expression.
 */
@Entity
@Table(name = "T_EXPRESSION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Expression extends AbstractWord implements Serializable {


    @OneToMany(mappedBy = "expression")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    protected Set<ExpressionTrans> expressionTranss = new HashSet<>();

    public Expression() {
    }

    public void setExpressionTranss(Set<ExpressionTrans> expressionTranss) {
        this.expressionTranss = expressionTranss;
    }

    public Set<ExpressionTrans> getExpressionTranss() {
        return expressionTranss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expression)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Expression that = (Expression) o;

        if (expressionTranss != null ? !expressionTranss.equals(that.expressionTranss)
                                     : that.expressionTranss != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (expressionTranss != null ? expressionTranss.hashCode() : 0);
        return result;
    }
}
