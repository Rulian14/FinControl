package com.fincontrol.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fincontrol.model.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{
    List<Receita> findByIdUsuario(Long idUsuario);   
}
