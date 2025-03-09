package com.example.kahvikauppa.model;

import org.springframework.data.jpa.domain.AbstractPersistable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tilaus extends AbstractPersistable<Long> {
    @Column(name = "order_details")
    private String orderDetails;
}
