package com.fincontrol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.auth.AuthResponseDTO;
import com.fincontrol.dto.auth.LoginDTO;
import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.dto.user.UserResponseDTO;
import com.fincontrol.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authservice;
    
    public AuthController(AuthService authservice){
        this.authservice = authservice;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO dto) {
        AuthResponseDTO response = authservice.registrar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto){
        AuthResponseDTO response = authservice.Login(dto);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> obterUsuarioLogado(Authentication authentication){
        Long idUsuario = Long.parseLong(authentication.getName());
        return ResponseEntity.status(200).body(authservice.obterUsuarioPorToken(idUsuario));
    }
}
