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

import com.fincontrol.dto.receita.ReceitaRequestDTO;
import com.fincontrol.dto.receita.ReceitaResponseDTO;
import com.fincontrol.service.ReceitaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/receitas")
public class ReceitasController{

    private final ReceitaService receitaService;

    public ReceitasController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @GetMapping
    public ResponseEntity<List<ReceitaResponseDTO>> ListarReceitas(Authentication authentication){
        Long idUsuarioLogado = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(receitaService.listarTodas(idUsuarioLogado));
    }

    @PostMapping
    public ResponseEntity<ReceitaResponseDTO> CadastrarReceita(@Valid @RequestBody ReceitaRequestDTO dto, Authentication authentication){
        Long idUsuarioLogado = Long.parseLong(authentication.getName());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(receitaService.criar(dto, idUsuarioLogado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaResponseDTO> AtualizarReceita(@PathVariable Long id, @RequestBody ReceitaRequestDTO dto, Authentication authentication){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeletarReceita( @PathVariable Long id, Authentication authentication){
        Long idUsuarioLogado = Long.parseLong(authentication.getName());
        receitaService.deletar(id, idUsuarioLogado);
        return ResponseEntity.noContent().build();
    }
   
}
