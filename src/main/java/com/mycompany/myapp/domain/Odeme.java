package com.mycompany.myapp.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Task entity.
 * @author The JHipster team.
 */
@ApiModel(description = "Task entity. @author The JHipster team.")
@Entity
@Table(name = "odeme")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "odeme")
public class Odeme implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "tarih")
    private LocalDate tarih;

    @Column(name = "odeme_adi")
    private String odemeAdi;

    @Column(name = "odeme_detayi")
    private String odemeDetayi;

    @Column(name = "odeme")
    private Long odeme;

    @ManyToOne
    @JsonIgnoreProperties("odemes")
    private Musteri musteri;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTarih() {
        return tarih;
    }

    public Odeme tarih(LocalDate tarih) {
        this.tarih = tarih;
        return this;
    }

    public void setTarih(LocalDate tarih) {
        this.tarih = tarih;
    }

    public String getOdemeAdi() {
        return odemeAdi;
    }

    public Odeme odemeAdi(String odemeAdi) {
        this.odemeAdi = odemeAdi;
        return this;
    }

    public void setOdemeAdi(String odemeAdi) {
        this.odemeAdi = odemeAdi;
    }

    public String getOdemeDetayi() {
        return odemeDetayi;
    }

    public Odeme odemeDetayi(String odemeDetayi) {
        this.odemeDetayi = odemeDetayi;
        return this;
    }

    public void setOdemeDetayi(String odemeDetayi) {
        this.odemeDetayi = odemeDetayi;
    }

    public Long getOdeme() {
        return odeme;
    }

    public Odeme odeme(Long odeme) {
        this.odeme = odeme;
        return this;
    }

    public void setOdeme(Long odeme) {
        this.odeme = odeme;
    }

    public Musteri getMusteri() {
        return musteri;
    }

    public Odeme musteri(Musteri musteri) {
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
        Odeme odeme = (Odeme) o;
        if (odeme.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), odeme.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Odeme{" +
            "id=" + getId() +
            ", tarih='" + getTarih() + "'" +
            ", odemeAdi='" + getOdemeAdi() + "'" +
            ", odemeDetayi='" + getOdemeDetayi() + "'" +
            ", odeme=" + getOdeme() +
            "}";
    }
}
