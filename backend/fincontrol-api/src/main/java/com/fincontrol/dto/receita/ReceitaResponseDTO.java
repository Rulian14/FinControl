package com.fincontrol.dto.receita;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceitaResponseDTO {
    
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Integer idCategoria;
    private String recorrencia;
}
