package com.example.kahvikauppa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.kahvikauppa.model.Tilaus;

public interface TilausRepository extends JpaRepository<Tilaus, Long> {
}
