package com.fincontrol.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fincontrol.dto.board.CategoriaTotalDTO;
import com.fincontrol.dto.board.DashboardDTO;
import com.fincontrol.dto.board.LancamentoDashboardProjection;
import com.fincontrol.dto.board.UltimosLancamentosDTO;
import com.fincontrol.repository.DespesaRepository;
import com.fincontrol.repository.ReceitaRepository;

@Service
public class DashBoardService {
    private final DespesaRepository despesaRepository;
    private final ReceitaRepository receitaRepository;

    public DashBoardService(DespesaRepository despesaRepository, ReceitaRepository receitaRepository){
        this.despesaRepository = despesaRepository;
        this.receitaRepository = receitaRepository;

    }
    @Transactional(readOnly = true)
    public DashboardDTO obterDashboardDTO(Long idUsuario, Integer ano, Integer mes){

        LocalDate inicioMes = LocalDate.of(ano, mes, 1);
        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fim = inicioMes.plusMonths(1).atStartOfDay();

        List<LancamentoDashboardProjection> receitArr =
                receitaRepository.buscarReceitasDashboard(idUsuario, inicio, fim);

        List<LancamentoDashboardProjection> despesArr =
                despesaRepository.buscarDespesasDashboard(idUsuario, inicio, fim);

        BigDecimal despesaTotal = despesArr.stream()
                                            .map(LancamentoDashboardProjection::getValor)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal receitaTotal = receitArr.stream()
                                            .map(LancamentoDashboardProjection::getValor)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = receitaTotal.subtract(despesaTotal);

        List<UltimosLancamentosDTO> ultLancamentoList = new ArrayList<>();
        
        ultLancamentoList.addAll(
            despesArr.stream()
                .map(d -> UltimosLancamentosDTO.builder()
                    .descricao(d.getDescricao())
                    .valor(d.getValor())
                    .data(d.getData())
                    .categoria(d.getCategoria())
                    .recorrencia(d.getRecorrencia())
                    .tipo("DESPESA")
                    .build()
                ).toList()
        );
        ultLancamentoList.addAll(
            receitArr.stream()
            .map(r -> UltimosLancamentosDTO.builder()
                    .descricao(r.getDescricao())
                    .valor(r.getValor())
                    .data(r.getData())
                    .categoria(r.getCategoria())
                    .recorrencia(r.getRecorrencia())
                    .tipo("RECEITA")
                    .build()
                ).toList()
        );
        ultLancamentoList = ultLancamentoList.stream()
        .sorted(Comparator.comparing(UltimosLancamentosDTO::getData).reversed())
        .toList();
        

        List<CategoriaTotalDTO> despesasPorCat = somarPorCategoria(despesArr);
        List<CategoriaTotalDTO> receitasPorCat = somarPorCategoria(receitArr);


        return DashboardDTO.builder()
            .ultLancamento(ultLancamentoList)
            .saldo(saldo)
            .despesaTotal(despesaTotal)
            .receitaTotal(receitaTotal)
            .ano(ano)
            .mes(mes)
            .despesasPorCategoria(despesasPorCat)
            .receitasPorCategoria(receitasPorCat)
            .build();
    }

    private List<CategoriaTotalDTO> somarPorCategoria(List<LancamentoDashboardProjection> lancamentos) {
        Map<String, BigDecimal> totaisPorCategoria = lancamentos.stream()
                .collect(Collectors.groupingBy(
                        LancamentoDashboardProjection::getCategoria,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                LancamentoDashboardProjection::getValor,
                                BigDecimal::add
                        )
                ));

        return totaisPorCategoria.entrySet().stream()
                .map(entry -> new CategoriaTotalDTO(entry.getKey(), entry.getValue()))
                .toList();
    }
}
