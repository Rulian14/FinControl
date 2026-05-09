package com.fincontrol.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.exception.EmailJaRegistradoException;
import com.fincontrol.model.Usuario;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void registrar(RegisterDTO dto){
        //colocar TryCatch dps
            String senhaHash = passwordEncoder.encode(dto.getSenha());

            Usuario usuario = new Usuario(dto.getNome(), dto.getEmail(), senhaHash, LocalDateTime.now());
            

    }





    //Regras de Negocio
    private boolean verificarEmail(String Email) throws EmailJaRegistradoException{
        return false;
    }

}
