package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Adres.
 */
@Entity
@Table(name = "adres")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "adres")
public class Adres implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ev_adres")
    private String evAdres;

    @Column(name = "is_adres")
    private String isAdres;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon_cep")
    private String telefonCep;

    @Column(name = "telefon_sabit")
    private String telefonSabit;

    @ManyToOne
    @JsonIgnoreProperties("adres")
    private Musteri musteri;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvAdres() {
        return evAdres;
    }

    public Adres evAdres(String evAdres) {
        this.evAdres = evAdres;
        return this;
    }

    public void setEvAdres(String evAdres) {
        this.evAdres = evAdres;
    }

    public String getIsAdres() {
        return isAdres;
    }

    public Adres isAdres(String isAdres) {
        this.isAdres = isAdres;
        return this;
    }

    public void setIsAdres(String isAdres) {
        this.isAdres = isAdres;
    }

    public String getEmail() {
        return email;
    }

    public Adres email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonCep() {
        return telefonCep;
    }

    public Adres telefonCep(String telefonCep) {
        this.telefonCep = telefonCep;
        return this;
    }

    public void setTelefonCep(String telefonCep) {
        this.telefonCep = telefonCep;
    }

    public String getTelefonSabit() {
        return telefonSabit;
    }

    public Adres telefonSabit(String telefonSabit) {
        this.telefonSabit = telefonSabit;
        return this;
    }

    public void setTelefonSabit(String telefonSabit) {
        this.telefonSabit = telefonSabit;
    }

    public Musteri getMusteri() {
        return musteri;
    }

    public Adres musteri(Musteri musteri) {
        this.musteri = musteri;
        return this;
    }

    public void setMusteri(Musteri musteri) {
        this.musteri = musteri;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Adres adres = (Adres) o;
        if (adres.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adres.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Adres{" +
            "id=" + getId() +
            ", evAdres='" + getEvAdres() + "'" +
            ", isAdres='" + getIsAdres() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefonCep='" + getTelefonCep() + "'" +
            ", telefonSabit='" + getTelefonSabit() + "'" +
            "}";
    }
}
