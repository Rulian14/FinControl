package com.fincontrol.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fincontrol.dto.board.LancamentoDashboardProjection;
import com.fincontrol.model.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{
    List<Receita> findByIdUsuario(Long idUsuario);   

    public List<Receita> findTop5ByIdUsuarioAndDataBetweenOrderByDataDesc(Long idUsuario, LocalDateTime inicio, LocalDateTime fim);

    @Query("""
    SELECT
        r.descricao AS descricao,
        r.valor AS valor,
        r.data AS data,
        COALESCE(c.nome, 'Sem Categoria') AS categoria,
        r.recorrencia AS recorrencia,
        'RECEITA' AS tipo
    FROM Receita r
    LEFT JOIN r.categoria c
    WHERE r.idUsuario = :idUsuario
      AND r.data >= :inicio
      AND r.data < :fim
    ORDER BY r.data DESC
    """)
    List<LancamentoDashboardProjection> buscarReceitasDashboard(
            @Param("idUsuario") Long idUsuario,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
