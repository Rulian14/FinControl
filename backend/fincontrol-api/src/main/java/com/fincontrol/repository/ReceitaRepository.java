package com.fincontrol.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fincontrol.model.Categoria;
import com.fincontrol.model.Receita;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long>{
    List<Receita> findByIdUsuario(Long idUsuario);   

     @Query("""
     SELECT SUM(r.valor)
        FROM Receita r
        WHERE r.idUsuario = :idUsuario
          AND r.data BETWEEN :inicio AND :fim
    """)
    BigDecimal sumReceita(@Param("idUsuario") Long idUsuario, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

      @Query("""
        SELECT c.nome AS nomeCategoria, SUM(r.valor) AS valorTotal
            FROM Receita r
            JOIN r.categoria c
            WHERE r.idUsuario = :idUsuario
            AND r.data BETWEEN :inicio AND :fim
            GROUP BY c.nome
    """)
    List<Categoria.CategoriaTotalProjection> somarReceitaPorCategoria(@Param("idUsuario") Long idUsuario, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim); 

    public List<Receita> findTop5ByIdUsuarioAndDataBetweenOrderByDataDesc(Long idUsuario, LocalDateTime inicio, LocalDateTime fim);

}
