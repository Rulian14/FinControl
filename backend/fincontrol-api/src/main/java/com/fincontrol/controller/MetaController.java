package com.fincontrol.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.meta.MetaRequestDTO;
import com.fincontrol.dto.meta.MetaResponseDTO;
import com.fincontrol.service.MetaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/metas")
@Validated
public class MetaController {

    private final MetaService metaService;

    public MetaController(MetaService metaService){
        this.metaService = metaService;
    }

    @GetMapping
    public ResponseEntity<List<MetaResponseDTO>> listarMetas(Authentication authentication){
        Long idUsuario = Long.parseLong(authentication.getName());
        return ResponseEntity.ok().body(metaService.listarTodasMetas(idUsuario));
    }
    @GetMapping("/{id}")
    public ResponseEntity<MetaResponseDTO> listarMeta(@PathVariable Long id, Authentication authentication){
        Long idUsuario = Long.parseLong(authentication.getName());
        return ResponseEntity.ok().body(metaService.listarMetaById(id, idUsuario));
    }
    
    @PostMapping
    public ResponseEntity<MetaResponseDTO> criarMeta(@Valid @RequestBody MetaRequestDTO metaDTO,
                                                     Authentication authentication){
        Long idUsuario = Long.parseLong(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(metaService.cadastrarMeta(idUsuario, metaDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MetaResponseDTO> atualizarMeta(@PathVariable Long id,
                                                         @Valid @RequestBody MetaRequestDTO metaDTO,
                                                         Authentication authentication){

        Long idUsuario = Long.parseLong(authentication.getName());
        return ResponseEntity.ok().body(metaService.atualizarMeta(id, idUsuario, metaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMeta(@PathVariable Long id, 
                                             Authentication authentication){

        Long idUsuario = Long.parseLong(authentication.getName());
        metaService.deletarMetaById(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}
