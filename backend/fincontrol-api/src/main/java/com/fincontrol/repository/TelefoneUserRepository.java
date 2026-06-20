package com.fincontrol.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fincontrol.model.TelefoneUsuario;
import com.fincontrol.model.TelefoneUsuario.telefoneProjection;

public interface TelefoneUserRepository extends JpaRepository<TelefoneUsuario, Long>{
    
    List<telefoneProjection> findByUsuarioId(Long idUsuario);

    Optional<TelefoneUsuario> findByIdAndUsuarioId(Long id, Long idUsuario);

    long countByUsuarioId(Long idUsuario);

    boolean existsByTelefone(String telefone);

    boolean existsByTelefoneAndIdNot(String telefone, Long id);


}
