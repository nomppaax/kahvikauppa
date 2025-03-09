package com.example.kahvikauppa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.kahvikauppa.model.Toimittaja;

public interface ToimittajaRepository extends JpaRepository<Toimittaja, Long> {
    Toimittaja findByName(String name);

    Toimittaja findByContactPersonEmail(String email);
}
