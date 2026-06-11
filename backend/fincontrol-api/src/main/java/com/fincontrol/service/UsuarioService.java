package com.fincontrol.service;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fincontrol.dto.user.TelefoneDTO;
import com.fincontrol.dto.user.TelefoneResponseDTO;
import com.fincontrol.dto.user.UserResponseDTO;
import com.fincontrol.dto.user.UserUpdateDTO;
import com.fincontrol.exception.EmailJaRegistradoException;
import com.fincontrol.exception.ResourceNotFoundException;
import com.fincontrol.exception.TelefoneJaCadastradoException;
import com.fincontrol.model.TelefoneUsuario;
import com.fincontrol.model.Usuario;
import com.fincontrol.repository.TelefoneUserRepository;
import com.fincontrol.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {
    private final TelefoneUserRepository telefoneUserRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(TelefoneUserRepository telefoneUserRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.telefoneUserRepository = telefoneUserRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    
    @Transactional
    public TelefoneResponseDTO cadastrarTelefone(Long idUsuario, TelefoneDTO dto){
        TelefoneUsuario telefoneUsuario = new TelefoneUsuario(dto.getTelefone(), 
                                                              idUsuario);

        verificarTelefone(dto.getTelefone());
        telefoneUserRepository.save(telefoneUsuario);
        return TelefoneResponseDTO.builder().telefone(telefoneUsuario.getTelefone()).id(telefoneUsuario.getId()).build();
    }

    @Transactional
    public TelefoneResponseDTO atualizarTelefone(Long idUsuario, Long id, TelefoneDTO dto){
        TelefoneUsuario telefoneUsuario = telefoneUserRepository.findByIdAndIdUsuario(id, idUsuario)
                                                                .orElseThrow(() -> new ResourceNotFoundException("Numero não encontrado"));
       
        verificarTelefoneParaAtualizacao(dto.getTelefone(), id);
        telefoneUsuario.setTelefone(dto.getTelefone());
        return TelefoneResponseDTO.builder().telefone(telefoneUsuario.getTelefone()).id(telefoneUsuario.getId()).build();                                     
    }
   
    @Transactional
    public UserResponseDTO atualizarDados(Long idUsuario, UserUpdateDTO dto){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                                           .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));

        boolean querAlterarNome = dto.getNome() != null && !dto.getNome().isBlank();

        boolean querAlterarEmail = dto.getEmail() != null && !dto.getEmail().isBlank();

        boolean querAlterarSenha = dto.getSenhaNova() != null && !dto.getSenhaNova().isBlank();

        boolean RequerSenhaValida = querAlterarEmail || querAlterarSenha;


        if (querAlterarNome) {
            usuario.setNome(dto.getNome());
        }

        if (RequerSenhaValida) {
            if (dto.getSenhaAtual() == null || dto.getSenhaAtual().isBlank()) {
                throw new BadCredentialsException("Para alterar e-mail ou senha, você deve informar a senha atual.");
            }
             boolean senhaValida = passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenhaHash());
            if (!senhaValida) {
                throw new BadCredentialsException("Credenciais inválidas.");
            }

            if (querAlterarEmail) {
                verificarEmail(dto.getEmail());
                usuario.setEmail(dto.getEmail());
            }

            if (querAlterarSenha) {
                String novaSenhaHash = passwordEncoder.encode(dto.getSenhaNova());
                usuario.setSenhaHash(novaSenhaHash);
            }

        }

        return converterParaResponseDTO(usuario);

    }
   
    //tenho q deixar esse metodo publico no AuthService e apenas injetar, para evitar logica duplicada
    private void verificarTelefone(String telefone){
        if (telefoneUserRepository.existsByTelefone(telefone)) {
            throw new TelefoneJaCadastradoException("telefone indisponivel");
        }
    }

    private void verificarTelefoneParaAtualizacao(String telefone, Long id) {
        if (telefoneUserRepository.existsByTelefoneAndIdNot(telefone, id)) {
            throw new TelefoneJaCadastradoException("Telefone indisponível");
        }
    }

    private void verificarEmail(String email) throws EmailJaRegistradoException{
       if (usuarioRepository.existsByEmail(email)) {
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

}

