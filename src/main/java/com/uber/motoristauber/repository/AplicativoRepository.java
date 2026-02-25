package com.uber.motoristauber.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uber.motoristauber.model.Aplicativo;

@Repository
public interface AplicativoRepository extends JpaRepository<Aplicativo, Integer> {

    @Query("select a from Aplicativo a where a.nom_aplicativo = :nom_aplicativo")
    Optional<Aplicativo> findByNom_aplicativo(@Param("nom_aplicativo") String nom_aplicativo);

    @Query("select a from Aplicativo a where a.tax_plataforma > :valor")
    List<Aplicativo> findByTax_plataformaGreaterThan(@Param("valor") BigDecimal valor);
}