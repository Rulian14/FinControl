package com.fincontrol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fincontrol.model.Meta;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long>{
    List<Meta> findByIdUsuario(Long idUsuario);

    boolean existsByIdAndIdUsuario(Long id, Long idUsuario);

    Optional<Meta> findByIdAndIdUsuario(Long id, Long idUsuario);
}
