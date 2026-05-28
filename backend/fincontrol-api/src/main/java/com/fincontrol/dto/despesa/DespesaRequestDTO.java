package com.fincontrol.dto.despesa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class DespesaRequestDTO{
    
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Integer idCategoria;
    private String recorrencia;
}