package com.example.kahvikauppa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kahvikauppa.model.Osasto;

public interface OsastoRepository extends JpaRepository<Osasto, Long> {
    Osasto findByName(String name);

    List<Osasto> findByOsastoIDP(Long osastoIDP);
}
