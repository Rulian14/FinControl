package com.fincontrol.dto.receita;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class ReceitaRequestDTO {

    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Integer idCategoria;
    private String recorrencia;
}
