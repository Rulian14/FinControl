package com.fincontrol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fincontrol.model.TelefoneUsuario;
import com.fincontrol.model.TelefoneUsuario.telefoneProjection;

public interface TelefoneUserRepository extends JpaRepository<TelefoneUsuario, Long>{
    List<telefoneProjection> findByIdUsuario(Long idUsuario);

    boolean existsByTelefone(String telefone);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);

    Optional<TelefoneUsuario> findByIdAndIdUsuario(Long id, Long idUsuario);
}
