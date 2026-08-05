package com.fincontrol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.user.TelefoneDTO;
import com.fincontrol.dto.user.TelefoneResponseDTO;
import com.fincontrol.dto.user.UserResponseDTO;
import com.fincontrol.dto.user.UserUpdateDTO;
import com.fincontrol.dto.user.UsuarioDeletarDTO;
import com.fincontrol.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/User")
public class UserController {
    private final UsuarioService usuarioService;

    public UserController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }
    @PostMapping("/telefones")
    public ResponseEntity<TelefoneResponseDTO> cadastrarNumeroTelefone(Authentication authentication, @Valid @RequestBody TelefoneDTO dto){
         Long idUsuario = Long.parseLong(authentication.getName());
         return ResponseEntity.status(201).body(usuarioService.cadastrarTelefone(idUsuario, dto));
    }
    
    @PostMapping("/telefones/{id}")
    public ResponseEntity<TelefoneResponseDTO> atualizarNumeroTelefone(Authentication authentication, @PathVariable Long id, @Valid @RequestBody TelefoneDTO dto){
         Long idUsuario = Long.parseLong(authentication.getName());
         return ResponseEntity.ok().body(usuarioService.atualizarTelefone(idUsuario, id, dto));
    }
    
    @DeleteMapping("/telefones/{id}")
    public ResponseEntity<Void> deletarNumeroTelefone(Authentication authentication, @PathVariable Long id){
        Long idUsuario = Long.parseLong(authentication.getName());
        usuarioService.deletarTelefone(idUsuario, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> atualizarDados(Authentication authentication, @Valid @RequestBody UserUpdateDTO dto){
        Long idUsuario = Long.parseLong(authentication.getName());
        return  ResponseEntity.ok().body(usuarioService.atualizarDados(idUsuario, dto));
        
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletarUsuario(Authentication authentication, @Valid @RequestBody UsuarioDeletarDTO dto){
        Long idUsuario = Long.parseLong(authentication.getName());
        usuarioService.deletarUsuario(idUsuario, dto);
        return ResponseEntity.noContent().build();
    }
}
