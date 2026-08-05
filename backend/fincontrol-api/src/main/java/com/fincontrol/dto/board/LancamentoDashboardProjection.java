package com.fincontrol.dto.board;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface LancamentoDashboardProjection {
    String getDescricao();

    BigDecimal getValor();

    LocalDateTime getData();

    String getCategoria();

    String getRecorrencia();

    String getTipo();
}
