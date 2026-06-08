package com.fincontrol.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fincontrol.model.Categoria;
import com.fincontrol.model.Despesa;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>{
    List<Despesa> findByIdUsuario(Long idUsuario);
    
    @Query("""
    SELECT SUM(d.valor)
    FROM Despesa d
    WHERE d.idUsuario = :idUsuario
      AND d.data BETWEEN :inicio AND :fim
    """)
    BigDecimal sumDespesa(@Param("idUsuario") Long idUsuario, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

      @Query("""
        SELECT c.nome AS nomeCategoria, SUM(d.valor) AS valorTotal
        FROM Despesa d
        JOIN d.categoria c
        WHERE d.idUsuario = :idUsuario
          AND d.data BETWEEN :inicio AND :fim
        GROUP BY c.nome
    """)
    List<Categoria.CategoriaTotalProjection> somarDespesasPorCategoria(@Param("idUsuario") Long idUsuario, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim); 

    public List<Despesa> findTop5ByIdUsuarioAndDataBetweenOrderByDataDesc(Long idUsuario, LocalDateTime inicio, LocalDateTime fim);


}
