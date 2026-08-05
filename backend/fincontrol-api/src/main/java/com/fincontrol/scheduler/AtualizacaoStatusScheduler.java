package com.fincontrol.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fincontrol.service.DespesaService;

@Component
public class AtualizacaoStatusScheduler {

    private final DespesaService despesaService;

    public AtualizacaoStatusScheduler(DespesaService despesaService) {
        this.despesaService = despesaService;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void atualizarDespesasVencidas() {
        int quantidadeAtualizada = despesaService.atualizarAgendadasVencidas();
        registrarResultado(quantidadeAtualizada);
    }


    private void registrarResultado(int quantidadeAtualizada) {
    }
}
    
