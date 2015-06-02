package com.gregdm.polco.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A WordValidation.
 */
@Entity
@Table(name = "T_WORDVALIDATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WordValidation extends AbstractWord implements Serializable {

    @Column(name = "translation")
    private String translation;

    @Column(name = "word_type")
    private String wordType;

    @Column(name = "number")
    private String number;

    @Column(name = "gender")
    private String gender;

    @Column(name = "person")
    private String person;

    @Column(name = "tense")
    private String tense;

    public WordValidation(){

    }

    public WordValidation(String word, String type){
        this.setValue(word);
        this.setWordType(type);
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTense() {
        return tense;
    }

    public void setTense(String tense) {
        this.tense = tense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordValidation wordValidation = (WordValidation) o;

        if (id != null ? !id.equals(wordValidation.id) : wordValidation.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "WordValidation{" +
                "id=" + id +
                ", value='" + value + "'" +
                ", translation='" + translation + "'" +
                ", wordType='" + wordType + "'" +
                ", number='" + number + "'" +
                ", gender='" + gender + "'" +
                ", person='" + person + "'" +
                ", tense='" + tense + "'" +
                '}';
    }
}
