package com.fincontrol.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fincontrol.dto.auth.AuthResponseDTO;
import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.dto.auth.loginDTO;
import com.fincontrol.dto.user.UserResponseDTO;
import com.fincontrol.exception.EmailJaRegistradoException;
import com.fincontrol.model.Usuario;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtservice;

    public AuthService(PasswordEncoder passwordEncoder, jwtService jwtservice) {
        this.passwordEncoder = passwordEncoder;
        this.jwtservice = jwtservice;
    }

    public AuthResponseDTO registrar(RegisterDTO dto){
        //colocar TryCatch dps
            String senhaHash = passwordEncoder.encode(dto.getSenha());

            Usuario usuario = new Usuario(dto.getNome(),
                                          dto.getEmail(), 
                                          senhaHash, 
                                          LocalDateTime.now());
            usuario.setId(1);
            String token = jwtservice.gerarToken(usuario);

            UserResponseDTO userDto = new UserResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
            AuthResponseDTO authresponseDTO = new AuthResponseDTO(token, "bearer", userDto);
            return authresponseDTO;
    }

    public String Login(loginDTO dto){
        return null;
    }


    //Regras de Negocio
    private boolean verificarEmail(String Email) throws EmailJaRegistradoException{
        return false;
    }

}
