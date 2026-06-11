package com.fincontrol.dto.despesa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class DespesaRequestDTO{
    @NotBlank
    private String descricao;
    @NotNull
    @Positive
    private BigDecimal valor;
    @NotNull
    @PastOrPresent
    private LocalDateTime data;
    private Integer idCategoria;
    @Pattern(regexp = "FIXA|TEMPORARIA", message = "deve ser FIXA ou TEMPORARIA")
    private String recorrencia;
}