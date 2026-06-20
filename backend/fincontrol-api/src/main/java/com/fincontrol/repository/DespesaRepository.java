package com.fincontrol.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fincontrol.dto.board.LancamentoDashboardProjection;
import com.fincontrol.model.Despesa;
import com.fincontrol.model.HistoricoDespesaProjection;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>{
    List<Despesa> findByUsuarioId(Long idUsuario);

    Optional<Despesa> findByIdAndUsuarioId(Long id, Long usuarioId);
    
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
    WHERE d.usuario.id = :idUsuario
        AND d.data >= :inicio
        AND d.data < :fim
      ORDER BY d.data DESC
    """)
    List<LancamentoDashboardProjection> buscarDespesasDashboard(
        @Param("idUsuario") Long idUsuario,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim);
//---------------------------------------------------------------
        //mexi em descrição tenho q esperar 
    @Query("""
        SELECT
            d.id AS despesaId,
            d.categoria.id AS categoriaId,
            d.categoria.nome AS categoriaNome,
            d.descricao AS descricao,
            d.valor AS valor,
            d.data AS data,
            d.status AS status
        FROM Despesa d
        WHERE d.usuario.id = :usuarioId
            AND d.recorrencia = 'FIXA'
            AND d.data BETWEEN :inicioPeriodo AND :fimPeriodo
        ORDER BY
            d.categoria.id,
            CASE
                WHEN d.status = 'PAGA' THEN 0
                WHEN d.status = 'PENDENTE' THEN 1
                ELSE 2
            END,
            d.data DESC
    """)
    List<HistoricoDespesaProjection> buscarHistoricoFixas(
            @Param("usuarioId") Long usuarioId,
            @Param("inicioPeriodo") LocalDateTime inicioPeriodo,
            @Param("fimPeriodo") LocalDateTime fimPeriodo
    );  

    public List<Despesa> findTop5ByUsuarioIdAndDataBetweenOrderByDataDesc(Long idUsuario, LocalDateTime inicio, LocalDateTime fim);


}
