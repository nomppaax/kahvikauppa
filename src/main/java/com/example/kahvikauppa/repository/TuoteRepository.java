package com.example.kahvikauppa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.kahvikauppa.model.Tuote;

public interface TuoteRepository extends JpaRepository<Tuote, Long> {

    @Query("SELECT t FROM Tuote t JOIN t.osasto o WHERE (o.id = :osastoID OR o.osastoIDP = :osastoID) AND LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Tuote> findByNameLikeIgnoreCaseAndOsastoId(@Param("keyword") String keyword, @Param("osastoID") Long osastoID);

    Tuote findByName(String name);

    @Query("SELECT t FROM Tuote t JOIN FETCH t.osasto o WHERE o.id = :osastoID OR o.osastoIDP = :osastoID")
    List<Tuote> findProductsByOsastoID(@Param("osastoID") Long osastoID);

    @Query("SELECT COUNT(t) FROM Tuote t WHERE t.toimittaja.id = :toimittajaID")
    Long countProductsByToimittajaID(@Param("toimittajaID") Long toimittajaID);

    @Query("SELECT COUNT(t) FROM Tuote t WHERE t.valmistaja.id = :valmistajaID")
    Long countProductsByValmistajaID(@Param("valmistajaID") Long valmistajaID);

    @Query("SELECT COUNT(t) FROM Tuote t WHERE t.osasto.id = :osastoID")
    Long countProductsByOsastoID(@Param("osastoID") Long osastoID);

}
