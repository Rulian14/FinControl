package com.fincontrol.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="meta")
@Entity
@Data
@NoArgsConstructor
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @NotNull
    @Min(0)
    @Column(name = "valor_objetivo", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorObjetivo;

    @NotNull
    @Min(0)
    @Column(name = "valor_contribuido", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorContribuido = BigDecimal.ZERO;

    @NotBlank
    @Size(max = 200)
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @NotNull
    @Future
    @Column(name = "data_limite", nullable = false)
    private LocalDate dataLimite;

    public Meta(Long idUsuario, BigDecimal valorObjetivo, BigDecimal valorContribuido, String nome, LocalDate dataLimite) {
        this.idUsuario = idUsuario;
        this.valorObjetivo = valorObjetivo;
        this.valorContribuido = valorContribuido;
        this.nome = nome;
        this.dataLimite = dataLimite;
    }
}
