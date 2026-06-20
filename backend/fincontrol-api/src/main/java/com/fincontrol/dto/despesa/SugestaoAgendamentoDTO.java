package com.fincontrol.dto.despesa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SugestaoAgendamentoDTO(
    String descricao,
    Long categoriaId,
    String categoriaNome,
    LocalDateTime data,
    BigDecimal valorSugerido,
    String recorrencia,
    String status


) {
    
}
