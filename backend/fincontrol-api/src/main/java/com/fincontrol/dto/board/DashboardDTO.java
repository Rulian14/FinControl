package com.fincontrol.dto.board;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardDTO {
    private List<UltimosLancamentosDTO> ultLancamento;
    private BigDecimal saldo;
    private BigDecimal despesaTotal;
    private BigDecimal receitaTotal;
    private int ano;
    private int mes;

    private List<CategoriaTotalDTO> despesasPorCategoria;
    private List<CategoriaTotalDTO> receitasPorCategoria;
}
