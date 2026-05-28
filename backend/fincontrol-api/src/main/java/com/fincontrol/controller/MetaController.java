package com.fincontrol.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.metas.MetasResponseDTO;

@RestController
@RequestMapping("/metas")
public class MetaController {

    @GetMapping
    public ResponseEntity<List<MetasResponseDTO>> Listar(){
        return null;
    }

    @PostMapping
    public ResponseEntity<MetasResponseDTO> criarMeta(){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMeta(@PathVariable Long id){
        return ResponseEntity.noContent().build();
    }
}
