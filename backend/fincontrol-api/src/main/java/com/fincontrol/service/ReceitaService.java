package com.fincontrol.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fincontrol.dto.receita.ReceitaRequestDTO;
import com.fincontrol.dto.receita.ReceitaResponseDTO;
import com.fincontrol.model.Categoria;
import com.fincontrol.model.Receita;
import com.fincontrol.repository.CategoriaRepository;
import com.fincontrol.repository.ReceitaRepository;

@Service
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final CategoriaRepository categoriaRepository;

    public ReceitaService(ReceitaRepository receitaRepository, CategoriaRepository categoriaRepository){
        this.receitaRepository = receitaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ReceitaResponseDTO> listarTodas(Integer idUsuarioAutenticado){
        return receitaRepository.findByIdUsuario(Long.valueOf(idUsuarioAutenticado))
                                .stream()
                                .map(this::converterParaResponseDTO)
                                .collect(Collectors.toList());
    }

    @Transactional
    public ReceitaResponseDTO criar(ReceitaRequestDTO dto, Integer idUsuarioAutenticado){
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("A categoria informada não habita este sistema."));

        // Se a categoria for de despesa, impedimos o vínculo com a receita
        if (!"RECEITA".equals(categoria.getTipo())) {
            throw new IllegalStateException("Esta categoria pertence ao mundo das despesas, não das receitas.");
        }

        Receita receita = new Receita(
            dto.getDescricao(),
            dto.getValor(),
            dto.getData(),
            dto.getRecorrencia(),
            idUsuarioAutenticado,
            categoria);

        Receita receitaSalva = receitaRepository.save(receita);
        return converterParaResponseDTO(receitaSalva);
    }

    @Transactional
    public void deletar(Long id, Integer idUsuarioAutenticado){
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada."));

        if (!receita.getIdUsuario().equals(idUsuarioAutenticado)) {
            throw new SecurityException("Você não tem poder para apagar este registro.");
        }
        receitaRepository.delete(receita);
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
