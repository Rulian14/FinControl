package com.fincontrol.dto.meta;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetaResponseDTO {

    private Long idMeta;
    private BigDecimal valorObjetivo;
    private BigDecimal valorContribuido;
    private BigDecimal porcentagemMeta;
    private String nome;
    private LocalDate dataLimite;
    private boolean atingida;

}
