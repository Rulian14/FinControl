package com.fincontrol.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fincontrol.dto.auth.AuthResponseDTO;
import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.dto.auth.loginDTO;
import com.fincontrol.exception.EmailJaRegistradoException;
import com.fincontrol.model.Usuario;
import com.fincontrol.repository.UsuarioRepository;

@Service
public class AuthService {

    @Value("${jwt.expiration}")
    private Long expiration;

    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtservice;
    private final UsuarioRepository usuariorepository;

    public AuthService(PasswordEncoder passwordEncoder, jwtService jwtservice, UsuarioRepository usuariorepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtservice = jwtservice;
        this.usuariorepository = usuariorepository;
    }

    public AuthResponseDTO registrar(RegisterDTO dto){
        //colocar TryCatch dps
            String senhaHash = passwordEncoder.encode(dto.getSenha());

            Usuario usuario = new Usuario(dto.getEmail(), 
                                          senhaHash, 
                                          LocalDateTime.now());

            usuariorepository.save(usuario);
                                          
            String token = jwtservice.gerarToken(usuario);
            AuthResponseDTO authresponseDTO = new AuthResponseDTO(token, "Bearer", expiration / 1000);
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
