package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "musteri")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "musteri")
public class Musteri implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ad")
    private String ad;

    @Column(name = "soyad")
    private String soyad;

    @OneToMany(mappedBy = "musteri")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Adres> adres = new HashSet<>();
    @OneToMany(mappedBy = "musteri")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Odeme> odemes = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "musteri_veli",
               joinColumns = @JoinColumn(name = "musteri_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "veli_id", referencedColumnName = "id"))
    private Set<Veli> velis = new HashSet<>();

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

    public Musteri ad(String ad) {
        this.ad = ad;
        return this;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public Musteri soyad(String soyad) {
        this.soyad = soyad;
        return this;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public Set<Adres> getAdres() {
        return adres;
    }

    public Musteri adres(Set<Adres> adres) {
        this.adres = adres;
        return this;
    }

    public Musteri addAdres(Adres adres) {
        this.adres.add(adres);
        adres.setMusteri(this);
        return this;
    }

    public Musteri removeAdres(Adres adres) {
        this.adres.remove(adres);
        adres.setMusteri(null);
        return this;
    }

    public void setAdres(Set<Adres> adres) {
        this.adres = adres;
    }

    public Set<Odeme> getOdemes() {
        return odemes;
    }

    public Musteri odemes(Set<Odeme> odemes) {
        this.odemes = odemes;
        return this;
    }

    public Musteri addOdeme(Odeme odeme) {
        this.odemes.add(odeme);
        odeme.setMusteri(this);
        return this;
    }

    public Musteri removeOdeme(Odeme odeme) {
        this.odemes.remove(odeme);
        odeme.setMusteri(null);
        return this;
    }

    public void setOdemes(Set<Odeme> odemes) {
        this.odemes = odemes;
    }

    public Set<Veli> getVelis() {
        return velis;
    }

    public Musteri velis(Set<Veli> velis) {
        this.velis = velis;
        return this;
    }

    public Musteri addVeli(Veli veli) {
        this.velis.add(veli);
        veli.getMusteris().add(this);
        return this;
    }

    public Musteri removeVeli(Veli veli) {
        this.velis.remove(veli);
        veli.getMusteris().remove(this);
        return this;
    }

    public void setVelis(Set<Veli> velis) {
        this.velis = velis;
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
        Musteri musteri = (Musteri) o;
        if (musteri.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), musteri.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Musteri{" +
            "id=" + getId() +
            ", ad='" + getAd() + "'" +
            ", soyad='" + getSoyad() + "'" +
            "}";
    }
}
