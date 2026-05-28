package com.fincontrol.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "receita")
@NoArgsConstructor
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_receita")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false, length = 20)
    private String recorrencia; // 'FIXA' ou 'TEMPORARIA'

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
    
    public Receita(String descricao, BigDecimal valor, LocalDateTime data, String recorrencia, Integer idUsuario, Categoria categoria) {
    this.descricao = descricao;
    this.valor = valor;
    this.data = data;
    this.recorrencia = recorrencia;
    this.idUsuario = idUsuario;
    this.categoria = categoria;
    }
}
