package com.fincontrol.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface HistoricoDespesaProjection {
    Long getDespesaId();

    Long getCategoriaId();

    String getCategoriaNome();

    BigDecimal getValor();

    LocalDateTime getData();

    StatusTransacao getStatus();

    String getDescricao();
    
}
