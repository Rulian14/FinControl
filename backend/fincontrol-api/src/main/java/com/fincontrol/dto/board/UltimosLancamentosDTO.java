package com.fincontrol.dto.board;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UltimosLancamentosDTO {
    
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private String categoria;
    private String recorrencia;
    private String tipo;
}
