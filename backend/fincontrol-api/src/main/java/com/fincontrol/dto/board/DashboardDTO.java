package com.fincontrol.dto.board;

import java.math.BigDecimal;
import java.util.List;

import com.fincontrol.model.Categoria;

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

    private List<Categoria.CategoriaTotalProjection> despesasPorCategoria;
    private List<Categoria.CategoriaTotalProjection> receitasPorCategoria;
}
