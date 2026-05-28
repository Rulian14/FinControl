package com.fincontrol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fincontrol.model.Despesa;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>{
    List<Despesa> findByIdUsuario(Long idUsuario);
}
