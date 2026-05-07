package com.fincontrol.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.dto.auth.loginDTO;

@RestController
@RequestMapping("/auth")
public class authController {

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO registrar){
        return registrar.getNome();
    }
    @PostMapping("/login")
    public void login(@RequestBody loginDTO logar){

    }
    @GetMapping("/me")
    public void rememerMe(){
        
    }
}
