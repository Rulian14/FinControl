package com.fincontrol.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fincontrol.dto.board.LancamentoDashboardProjection;
import com.fincontrol.model.Despesa;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>{
    List<Despesa> findByIdUsuario(Long idUsuario);
    
    @Query("""
    SELECT
        d.descricao AS descricao,
        d.valor AS valor,
        d.data AS data,
        COALESCE(c.nome, 'Sem Categoria') AS categoria,
        d.recorrencia AS recorrencia,
        'DESPESA' AS tipo
    FROM Despesa d
    LEFT JOIN d.categoria c
    WHERE d.idUsuario = :idUsuario
        AND d.data >= :inicio
        AND d.data < :fim
      ORDER BY d.data DESC
    """)
    List<LancamentoDashboardProjection> buscarDespesasDashboard(
        @Param("idUsuario") Long idUsuario,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim);

    public List<Despesa> findTop5ByIdUsuarioAndDataBetweenOrderByDataDesc(Long idUsuario, LocalDateTime inicio, LocalDateTime fim);


}
