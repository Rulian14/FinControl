package com.fincontrol.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fincontrol.dto.despesa.DespesaResponseDTO;
import com.fincontrol.dto.despesa.DespesaRequestDTO;
import com.fincontrol.model.Categoria;
import com.fincontrol.model.Despesa;
import com.fincontrol.repository.CategoriaRepository;
import com.fincontrol.repository.DespesaRepository;


@Service
public class DespesaService {
    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;

    public DespesaService(DespesaRepository despesaRepository, CategoriaRepository categoriaRepository){
        this.despesaRepository = despesaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<DespesaResponseDTO> listarTodas(Integer idUsuarioAutenticado){
        return despesaRepository.findByIdUsuario(Long.valueOf(idUsuarioAutenticado))
                                .stream()
                                .map(this::converterParaResponseDTO)
                                .collect(Collectors.toList());
    }

    @Transactional
    public DespesaResponseDTO criar(DespesaRequestDTO dto, Integer idUsuarioAutenticado){
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("A categoria informada não habita este sistema."));

        if (!"DESPESA".equals(categoria.getTipo())) {
            throw new IllegalStateException("Esta categoria pertence ao mundo das despesas, não das receitas.");
        }
        Despesa despesa = new Despesa(
            dto.getDescricao(),
            dto.getValor(),
            dto.getData(),
            dto.getRecorrencia(),
            idUsuarioAutenticado,
            categoria);

        Despesa despesaSalva = despesaRepository.save(despesa);
        return converterParaResponseDTO(despesaSalva);
    }

    public void deletar(Long id, Integer idUsuarioAutenticado){
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("despesa não encontrada."));

        if (!despesa.getIdUsuario().equals(idUsuarioAutenticado)) {
            throw new SecurityException("Você não tem poder para apagar este registro.");
        }
        despesaRepository.delete(despesa);
    }

     private DespesaResponseDTO converterParaResponseDTO(Despesa despesa) {
        return DespesaResponseDTO.builder()
            .id(despesa.getId())
            .descricao(despesa.getDescricao())
            .valor(despesa.getValor())
            .data(despesa.getData())
            .recorrencia(despesa.getRecorrencia())
            .idCategoria(despesa.getCategoria().getId())
            .build();
    }
}
