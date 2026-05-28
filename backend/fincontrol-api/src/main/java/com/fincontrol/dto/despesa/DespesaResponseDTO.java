package com.fincontrol.dto.despesa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class DespesaResponseDTO {
    
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Integer idCategoria;
    private String recorrencia;
}
