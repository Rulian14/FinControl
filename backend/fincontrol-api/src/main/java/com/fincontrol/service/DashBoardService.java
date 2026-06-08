package com.fincontrol.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fincontrol.dto.board.DashboardDTO;
import com.fincontrol.dto.board.UltimosLancamentosDTO;
import com.fincontrol.model.Categoria;
import com.fincontrol.model.Despesa;
import com.fincontrol.model.Receita;
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
        LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fim = fimMes.atTime(23, 59, 59, 999999999);

        BigDecimal despesa = despesaRepository.sumDespesa(idUsuario, inicio, fim);
        BigDecimal receita = receitaRepository.sumReceita(idUsuario, inicio, fim);

        BigDecimal despesaTotal = despesa == null ? BigDecimal.ZERO : despesa;
        BigDecimal receitaTotal = receita == null ? BigDecimal.ZERO : receita;
        
        BigDecimal saldo = receitaTotal.subtract(despesaTotal);

        List<Receita> receitArr = receitaRepository.findTop5ByIdUsuarioAndDataBetweenOrderByDataDesc(idUsuario, inicio, fim);
        List<Despesa> despesArr = despesaRepository.findTop5ByIdUsuarioAndDataBetweenOrderByDataDesc(idUsuario, inicio, fim);

        List<UltimosLancamentosDTO> ultLancamentoList = new ArrayList<>();
        
        ultLancamentoList.addAll(
            despesArr.stream()
                .map(d -> UltimosLancamentosDTO.builder()
                    .descricao(d.getDescricao())
                    .valor(d.getValor())
                    .data(d.getData())
                    .categoria(d.getCategoria() != null ? d.getCategoria().getNome() : "Sem Categoria")
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
                    .categoria(r.getCategoria() != null ? r.getCategoria().getNome() : "Sem Categoria")
                    .recorrencia(r.getRecorrencia())
                    .tipo("RECEITA")
                    .build()
                ).toList()
        );
        ultLancamentoList = ultLancamentoList.stream()
        .sorted(Comparator.comparing(UltimosLancamentosDTO::getData).reversed())
        .limit(5)
        .toList();

        List<Categoria.CategoriaTotalProjection> despesasPorCat = despesaRepository.somarDespesasPorCategoria(idUsuario, inicio, fim);
        List<Categoria.CategoriaTotalProjection> receitasPorCat = receitaRepository.somarReceitaPorCategoria(idUsuario, inicio, fim);


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

}
