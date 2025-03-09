package com.example.kahvikauppa.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.AbstractPersistable;

import jakarta.annotation.Nonnull;
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
@Table(name = "osasto_details")
public class Osasto extends AbstractPersistable<Long> {

    @Column(name = "name", nullable = false)
    private String name;

    @Nonnull
    @Column(name = "osastoIDP")
    private Long osastoIDP; // Yläosasto, null jos osasto on pääosasto

    @Column(name = "productCount", nullable = false)
    private int productCount = 0;

    @OneToMany(mappedBy = "osasto")
    private List<Tuote> products = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("Osasto{id=%d, name='%s', osastoIDP=%d, productCount=%d}", getId(), name, osastoIDP,
                productCount);
    }
}
