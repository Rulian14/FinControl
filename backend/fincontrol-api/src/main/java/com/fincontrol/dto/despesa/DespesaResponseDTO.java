package com.fincontrol.dto.despesa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fincontrol.model.StatusTransacao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DespesaResponseDTO {
    
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime data;
    private Integer idCategoria;
    private String recorrencia;
    private StatusTransacao status;
}

