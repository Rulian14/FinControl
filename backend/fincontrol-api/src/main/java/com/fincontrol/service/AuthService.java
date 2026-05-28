package com.fincontrol.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fincontrol.dto.auth.AuthResponseDTO;
import com.fincontrol.dto.auth.LoginDTO;
import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.dto.user.UserResponseDTO;
import com.fincontrol.exception.EmailJaRegistradoException;
import com.fincontrol.model.Usuario;
import com.fincontrol.repository.UsuarioRepository;

@Service
public class AuthService {

    @Value("${jwt.expiration}")
    private Long expiration;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtservice;
    private final UsuarioRepository usuariorepository;

    public AuthService(PasswordEncoder passwordEncoder, JwtService jwtservice, UsuarioRepository usuariorepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtservice = jwtservice;
        this.usuariorepository = usuariorepository;
    }

    public AuthResponseDTO registrar(RegisterDTO dto){

            verificarEmail(dto.getEmail());
            String senhaHash = passwordEncoder.encode(dto.getSenha());

            Usuario usuario = new Usuario(dto.getNome(),
                                          dto.getEmail(), 
                                          senhaHash, 
                                          LocalDateTime.now());

            usuariorepository.save(usuario);                   
            String token = jwtservice.gerarToken(usuario);
            AuthResponseDTO authresponseDTO = new AuthResponseDTO(token, "Bearer", expiration / 1000);
            return authresponseDTO; 
    }

    public AuthResponseDTO Login(LoginDTO dto){
        Usuario usuario = usuariorepository.findByEmail(dto.getEmail())
                                                            .orElseThrow(() -> new BadCredentialsException("Crendecias invalidas"));

        boolean senhaValida = passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash());
            if(!senhaValida){
             throw new BadCredentialsException("Crendecias invalidas");
            }
        String token = jwtservice.gerarToken(usuario);
        AuthResponseDTO authresponseDTO = new AuthResponseDTO(token, "Bearer", expiration / 1000);
        return authresponseDTO;
    }
    public UserResponseDTO obterUsuarioPorToken(String token){
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BadCredentialsException("token no formato invalido");
        }
        String tokenPuro = token.substring(7);

        if (!jwtservice.validarToken(tokenPuro)) {
            throw new BadCredentialsException("token expirado.");
        }
        String idString = jwtservice.extrairID(tokenPuro);
        Integer id = Integer.parseInt(idString);


        Usuario usuario = usuariorepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Usuário foi engolido pelo vazio."));
        return new UserResponseDTO( usuario.getId(), usuario.getNome(), usuario.getEmail());

    }


    //Regras de Negocio
    private void verificarEmail(String email) throws EmailJaRegistradoException{
       if (usuariorepository.existsByEmail(email)) {
            throw new EmailJaRegistradoException("O endereço de e-mail já está associado a uma conta.");
        }
    }

}
