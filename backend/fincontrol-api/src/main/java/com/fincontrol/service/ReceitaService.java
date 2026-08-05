package com.fincontrol.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fincontrol.dto.receita.ReceitaRequestDTO;
import com.fincontrol.dto.receita.ReceitaResponseDTO;
import com.fincontrol.exception.CategoriaInvalidaException;
import com.fincontrol.exception.ResourceNotFoundException;
import com.fincontrol.model.Categoria;
import com.fincontrol.model.Receita;
import com.fincontrol.model.Usuario;
import com.fincontrol.repository.CategoriaRepository;
import com.fincontrol.repository.ReceitaRepository;
import com.fincontrol.repository.UsuarioRepository;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final CategoriaRepository categoriaRepository;
      private final UsuarioRepository usuarioRepository;


    public ReceitaService(ReceitaRepository receitaRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository){
        this.receitaRepository = receitaRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    } 

    @Transactional(readOnly = true)
    public List<ReceitaResponseDTO> listarTodas(Long idUsuarioAutenticado){
        return receitaRepository.findByUsuarioId(Long.valueOf(idUsuarioAutenticado))
                                .stream()
                                .map(this::converterParaResponseDTO)
                                .collect(Collectors.toList());
    }

    @Transactional
    public ReceitaResponseDTO criar(ReceitaRequestDTO dto, Long idUsuarioAutenticado){
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("A categoria informada não habita este sistema."));
        Usuario usuario = usuarioRepository.getReferenceById(idUsuarioAutenticado);

        verificarCategoria(categoria);

        Receita receita = new Receita(
            dto.getDescricao(),
            dto.getValor(),
            dto.getData(),
            dto.getRecorrencia(),
            usuario,
            categoria);

        Receita receitaSalva = receitaRepository.save(receita);
        return converterParaResponseDTO(receitaSalva);
    }

    @Transactional
    public ReceitaResponseDTO atualizar(ReceitaRequestDTO dto, Long idUsuarioAutenticado, Long id){
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("A categoria informada não habita este sistema."));

        verificarCategoria(categoria);
        
        Receita receitaExistente = receitaRepository
            .findByIdAndUsuarioId(id, idUsuarioAutenticado)
            .orElseThrow(() -> new ResourceNotFoundException("Receita não encontrada."));
                receitaExistente.setDescricao(dto.getDescricao());
                receitaExistente.setValor(dto.getValor());
                receitaExistente.setData(dto.getData());
                receitaExistente.setRecorrencia(dto.getRecorrencia());
                receitaExistente.setCategoria(categoria);
           
        return converterParaResponseDTO(receitaExistente);
    }

    @Transactional
    public void deletar(Long id, Long idUsuarioAutenticado){
        Receita receita = receitaRepository
            .findByIdAndUsuarioId(id, idUsuarioAutenticado)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Despesa não encontrada ou não pertence ao usuário."
            ));
        receitaRepository.delete(receita);
    }

    //conversão e regras de negocio
    private void verificarCategoria(Categoria categoria){
        if (!"RECEITA".equals(categoria.getTipo())) {
            throw new CategoriaInvalidaException("Esta categoria pertence ao mundo das despesas, não das receitas.");
        }
    }


    private ReceitaResponseDTO converterParaResponseDTO(Receita receita) {
        return ReceitaResponseDTO.builder()
            .id(receita.getId())
            .descricao(receita.getDescricao())
            .valor(receita.getValor())
            .data(receita.getData())
            .recorrencia(receita.getRecorrencia())
            .idCategoria(receita.getCategoria().getId())
            .build();
    }
}
