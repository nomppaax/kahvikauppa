package com.example.kahvikauppa.model;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "tuote_details")
public class Tuote extends AbstractPersistable<Long> {

    @Column(name = "name")
    private String name;
    // ALTER TABLE TUOTE_DETAILS ALTER COLUMN NAME VARCHAR(500)

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    // muokattu h2-tietokantaan kuvaus-kentän merkkien pituutta, jotta käyttäjä
    // kirjoittaa pidemmän kuvauksen
    // ALTER TABLE TUOTE_DETAILS ALTER COLUMN DESCRIPTION VARCHAR(2000);

    @Nonnull
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "productImage")
    @Lob
    private byte[] productImage;

    @ManyToOne
    private Osasto osasto;

    @ManyToOne
    private Toimittaja toimittaja;

    @ManyToOne
    private Valmistaja valmistaja;
}