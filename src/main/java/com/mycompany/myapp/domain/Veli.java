package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Veli.
 */
@Entity
@Table(name = "veli")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "veli")
public class Veli implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ad")
    private String ad;

    @Column(name = "soyad")
    private String soyad;

    @Column(name = "adres")
    private String adres;

    @Column(name = "meslegi")
    private String meslegi;

    @Column(name = "email")
    private String email;

    @Column(name = "telefon_cep")
    private String telefonCep;

    @Column(name = "telefon_sabit")
    private String telefonSabit;

    @ManyToMany(mappedBy = "velis")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Musteri> musteris = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public Veli ad(String ad) {
        this.ad = ad;
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public Veli soyad(String soyad) {
        this.soyad = soyad;
        return this;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getAdres() {
        return adres;
    }

    public Veli adres(String adres) {
        this.adres = adres;
        return this;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getMeslegi() {
        return meslegi;
    }

    public Veli meslegi(String meslegi) {
        this.meslegi = meslegi;
        return this;
    }

    public void setMeslegi(String meslegi) {
        this.meslegi = meslegi;
    }

    public String getEmail() {
        return email;
    }

    public Veli email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonCep() {
        return telefonCep;
    }

    public Veli telefonCep(String telefonCep) {
        this.telefonCep = telefonCep;
        return this;
    }

    public void setTelefonCep(String telefonCep) {
        this.telefonCep = telefonCep;
    }

    public String getTelefonSabit() {
        return telefonSabit;
    }

    public Veli telefonSabit(String telefonSabit) {
        this.telefonSabit = telefonSabit;
        return this;
    }

    public void setTelefonSabit(String telefonSabit) {
        this.telefonSabit = telefonSabit;
    }

    public Set<Musteri> getMusteris() {
        return musteris;
    }

    public Veli musteris(Set<Musteri> musteris) {
        this.musteris = musteris;
        return this;
    }

    public Veli addMusteri(Musteri musteri) {
        this.musteris.add(musteri);
        musteri.getVelis().add(this);
        return this;
    }

    public Veli removeMusteri(Musteri musteri) {
        this.musteris.remove(musteri);
        musteri.getVelis().remove(this);
        return this;
    }

    public void setMusteris(Set<Musteri> musteris) {
        this.musteris = musteris;
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
        Veli veli = (Veli) o;
        if (veli.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), veli.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Veli{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            ", soyad='" + getSoyad() + "'" +
            ", adres='" + getAdres() + "'" +
            ", meslegi='" + getMeslegi() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefonCep='" + getTelefonCep() + "'" +
            ", telefonSabit='" + getTelefonSabit() + "'" +
            "}";
    }
}
