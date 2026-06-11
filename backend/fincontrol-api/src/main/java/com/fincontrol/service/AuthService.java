package com.fincontrol.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fincontrol.dto.auth.AuthResponseDTO;
import com.fincontrol.dto.auth.LoginDTO;
import com.fincontrol.dto.auth.RegisterDTO;
import com.fincontrol.dto.user.UserResponseDTO;
import com.fincontrol.exception.EmailJaRegistradoException;
import com.fincontrol.model.TelefoneUsuario;
import com.fincontrol.model.Usuario;
import com.fincontrol.repository.TelefoneUserRepository;
import com.fincontrol.repository.UsuarioRepository;


@Service
public class AuthService {

    @Value("${jwt.expiration}")
    private Long expiration;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtservice;
    private final UsuarioRepository usuariorepository;
    private final TelefoneUserRepository telefoneUserRepository;

    public AuthService(PasswordEncoder passwordEncoder, JwtService jwtservice, UsuarioRepository usuariorepository, TelefoneUserRepository telefoneUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtservice = jwtservice;
        this.usuariorepository = usuariorepository;
        this.telefoneUserRepository = telefoneUserRepository;

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

            return converterParaResponseDTO(token, usuario);
            }

    public AuthResponseDTO Login(LoginDTO dto){
        Usuario usuario = usuariorepository.findByEmail(dto.getEmail())
                                                            .orElseThrow(() -> new BadCredentialsException("Crendecias invalidas"));

        boolean senhaValida = passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash());
            if(!senhaValida){
             throw new BadCredentialsException("Crendecias invalidas");
            }
        String token = jwtservice.gerarToken(usuario);

        return converterParaResponseDTO(token, usuario);
    }
    //arrumar dps
    public UserResponseDTO obterUsuarioPorToken(Long idUsuario){
        Usuario usuario = usuariorepository.findById(idUsuario)
                                           .orElseThrow(() -> new RuntimeException("Usuário foi engolido pelo vazio."));
                            
        return converterParaResponseDTO(usuario);
    }


    //Regras de Negocio
    private void verificarEmail(String email) throws EmailJaRegistradoException{
       if (usuariorepository.existsByEmail(email)) {
            throw new EmailJaRegistradoException("O endereço de e-mail já está associado a uma conta.");
        }
    }


    private UserResponseDTO converterParaResponseDTO(Usuario usuario){
        List<TelefoneUsuario.telefoneProjection> telefones = telefoneUserRepository.findByIdUsuario(usuario.getId());       
        return UserResponseDTO.builder()
                              .email(usuario.getEmail())
                              .nome(usuario.getNome())
                              .telefones(telefones)
                              .build();
    }

    private AuthResponseDTO converterParaResponseDTO(String token, Usuario usuario){
        List<TelefoneUsuario.telefoneProjection> telefones = telefoneUserRepository.findByIdUsuario(usuario.getId());  
        return AuthResponseDTO.builder()
                              .token(token)
                              .tipo("Bearer")
                              .expiration(expiration / 1000)
                              .userResponseDTO(converterParaResponseDTO(usuario))
                              .build();
    }
}
