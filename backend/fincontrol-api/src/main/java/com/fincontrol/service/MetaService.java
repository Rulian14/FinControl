package com.fincontrol.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fincontrol.dto.meta.MetaRequestDTO;
import com.fincontrol.dto.meta.MetaResponseDTO;
import com.fincontrol.exception.ResourceNotFoundException;
import com.fincontrol.model.Meta;
import com.fincontrol.repository.MetaRepository;

@Service
public class MetaService {

    private final MetaRepository metaRepository;

    public MetaService(MetaRepository metaRepository){
        this.metaRepository = metaRepository;
    }
  
    @Transactional(readOnly = true)
    public List<MetaResponseDTO> listarTodasMetas(Long idUsuario){
        return metaRepository.findByIdUsuario(idUsuario)
            .stream()
            .map(this::converterParaResponseDTO)
            .toList();
    }

    @Transactional(readOnly = true)
    public MetaResponseDTO listarMetaById(Long id, Long idUsuario){
        Meta meta = metaRepository.findByIdAndIdUsuario(id, idUsuario)
                                  .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));
        return converterParaResponseDTO(meta);
    }
    
    @Transactional
    public MetaResponseDTO cadastrarMeta(Long idUsuario, MetaRequestDTO dto){
        Meta meta = new Meta(
            idUsuario,
            dto.getValorObjetivo(),
            dto.getValorContribuido(),
            dto.getNome(),
            dto.getDataLimite()
        );

        Meta metaSalva = metaRepository.save(meta);
        return converterParaResponseDTO(metaSalva);
    }

    @Transactional
    public MetaResponseDTO atualizarMeta(Long id, Long idUsuario, MetaRequestDTO dto){
        Meta meta = metaRepository.findByIdAndIdUsuario(id, idUsuario)
                                  .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));

        meta.setNome(dto.getNome());
        meta.setValorObjetivo(dto.getValorObjetivo());
        meta.setValorContribuido(dto.getValorContribuido());
        meta.setDataLimite(dto.getDataLimite());

        return converterParaResponseDTO(meta);
    }

    @Transactional
    public void deletarMetaById(Long id, Long idUsuario){
        Meta meta = metaRepository.findByIdAndIdUsuario(id, idUsuario)
                                  .orElseThrow(() -> new ResourceNotFoundException("Meta não encontrada"));
        metaRepository.delete(meta);
    }

    //regras de negocios e utilites
    private BigDecimal porcentagem(BigDecimal valorObjetivo, BigDecimal valorAtual){
        return valorAtual.divide(valorObjetivo, 4, RoundingMode.HALF_UP)
                                   .multiply(BigDecimal.valueOf(100))
                                   .setScale(2, RoundingMode.HALF_UP);
    } 

    private MetaResponseDTO converterParaResponseDTO(Meta meta){
            BigDecimal porCem = porcentagem(
                 meta.getValorObjetivo(),
                 meta.getValorContribuido()
            );

            return MetaResponseDTO.builder()
                                .idMeta(meta.getId())
                                .valorObjetivo(meta.getValorObjetivo())
                                .valorContribuido(meta.getValorContribuido())
                                .porcentagemMeta(porCem)
                                .nome(meta.getNome())
                                .dataLimite(meta.getDataLimite())
                                .atingida(porCem.compareTo(BigDecimal.valueOf(100)) >= 0)
                                .build();
    }
}
