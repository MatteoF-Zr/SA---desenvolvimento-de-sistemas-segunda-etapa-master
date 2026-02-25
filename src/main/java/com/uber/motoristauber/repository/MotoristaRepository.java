package com.uber.motoristauber.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uber.motoristauber.model.Motorista;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Integer> {

    @Query("select m from Motorista m where m.cpf_motorista = :cpf_motorista")
    Optional<Motorista> findByCpf_motorista(@Param("cpf_motorista") String cpf_motorista);

    @Query("select m from Motorista m where m.pla_veiculo = :pla_veiculo")
    List<Motorista> findByPla_veiculo(@Param("pla_veiculo") String pla_veiculo);
}