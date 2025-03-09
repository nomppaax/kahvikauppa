package com.example.kahvikauppa.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "valmistaja_details")
public class Valmistaja extends AbstractPersistable<Long> {
    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "productCount")
    private Integer productCount;

    @OneToMany(mappedBy = "valmistaja")
    private List<Tuote> products = new ArrayList<>();
}
