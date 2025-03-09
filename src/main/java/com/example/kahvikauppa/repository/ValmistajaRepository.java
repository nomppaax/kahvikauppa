package com.example.kahvikauppa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kahvikauppa.model.Valmistaja;

public interface ValmistajaRepository extends JpaRepository<Valmistaja, Long> {
    Valmistaja findByName(String name);
}
