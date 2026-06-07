package com.fincontrol.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.despesa.DespesaRequestDTO;
import com.fincontrol.dto.despesa.DespesaResponseDTO;
import com.fincontrol.service.DespesaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/despesas")
public class DespesasController {

    private final DespesaService despesaService;

    public DespesasController(DespesaService despesaService){
        this.despesaService = despesaService;
    }

    @GetMapping
    public ResponseEntity<List<DespesaResponseDTO>> ListarDespesas(Authentication authentication){
        Integer idUsuarioLogado = Integer.parseInt(authentication.getName());
        return ResponseEntity.ok(despesaService.listarTodas(idUsuarioLogado));
    }

    @PostMapping
    public ResponseEntity<DespesaResponseDTO> CadastrarDespesas(@Valid @RequestBody DespesaRequestDTO dto, Authentication authentication){
        Integer idUsuarioLogado = Integer.parseInt(authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(despesaService.criar(dto, idUsuarioLogado));
    }
    @PutMapping("/{id}")
    public ResponseEntity<DespesaResponseDTO> AtualizarDespesa(
        @PathVariable Long id,
        @RequestBody DespesaRequestDTO dto){
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeletarDespesa(@PathVariable Long id, Authentication authentication){
        Integer idUsuarioLogado = Integer.parseInt(authentication.getName());
        despesaService.deletar(id, idUsuarioLogado);
        return ResponseEntity.noContent().build();
    }
}
