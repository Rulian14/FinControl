package com.fincontrol.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.auth.AuthResponseDTO;
import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.dto.auth.loginDTO;
import com.fincontrol.service.AuthService;

@RestController
@RequestMapping("/auth")
public class authController {

    private final AuthService authservice;
    
    public authController(AuthService authservice){
        this.authservice = authservice;
    }
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterDTO dto) {
        AuthResponseDTO response = authservice.registrar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public void login(@RequestBody loginDTO logar){

    }

    @PostMapping("/test")
    public String testarToken(@RequestBody Map<String, String> body) {
        return authservice.test(body.get("token"));
    }
    @PostMapping("/testDois")
    public boolean validarToken(@RequestBody Map<String, String> body){
        return authservice.testDois(body.get("token"));
    }
    @GetMapping("/me")
    public void rememerMe(){
    }

}
