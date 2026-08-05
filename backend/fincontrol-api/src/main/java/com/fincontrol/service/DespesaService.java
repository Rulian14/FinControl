package com.fincontrol.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fincontrol.dto.despesa.DespesaRequestDTO;
import com.fincontrol.dto.despesa.DespesaResponseDTO;
import com.fincontrol.dto.despesa.SugestaoAgendamentoDTO;
import com.fincontrol.exception.AgendarDespesaPassadoException;
import com.fincontrol.exception.ResourceNotFoundException;
import com.fincontrol.model.Categoria;
import com.fincontrol.model.Despesa;
import com.fincontrol.model.HistoricoDespesaProjection;
import com.fincontrol.model.StatusTransacao;
import com.fincontrol.model.Usuario;
import com.fincontrol.repository.CategoriaRepository;
import com.fincontrol.repository.DespesaRepository;
import com.fincontrol.repository.UsuarioRepository;


@Service
public class DespesaService {
    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public DespesaService(DespesaRepository despesaRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository){
        this.despesaRepository = despesaRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<DespesaResponseDTO> listarTodas(Long idUsuarioAutenticado){
        return despesaRepository.findByUsuarioId(Long.valueOf(idUsuarioAutenticado))
                                .stream()
                                .map(this::converterParaResponseDTO)
                                .collect(Collectors.toList());
    }

    @Transactional
    public DespesaResponseDTO criar(DespesaRequestDTO dto, Long idUsuarioAutenticado){
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("A categoria informada não habita este sistema."));
        Usuario usuario = usuarioRepository.getReferenceById(idUsuarioAutenticado);

        verificarCategoria(categoria);

        validarDataAgendamento(dto);

        Despesa despesa = new Despesa(
            dto.getDescricao(),
            dto.getValor(),
            dto.getData(),
            dto.getRecorrencia(),
            usuario,
            categoria,
            dto.getStatus()
        );

        Despesa despesaSalva = despesaRepository.save(despesa);
        return converterParaResponseDTO(despesaSalva);
    }
    
    @Transactional
    public DespesaResponseDTO atualizar(DespesaRequestDTO dto, Long idUsuarioAutenticado, Long id){
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("A categoria informada não habita este sistema."));

        verificarCategoria(categoria);

        validarDataAgendamento(dto);

        Despesa despesaExistente = despesaRepository
            .findByIdAndUsuarioId(id, idUsuarioAutenticado)
            .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada."));
                despesaExistente.setDescricao(dto.getDescricao());
                despesaExistente.setValor(dto.getValor());
                despesaExistente.setData(dto.getData());
                despesaExistente.setRecorrencia(dto.getRecorrencia());
                despesaExistente.setCategoria(categoria);
                despesaExistente.setStatus(dto.getStatus());


        return converterParaResponseDTO(despesaExistente);
    }

    @Transactional
    public void deletar(Long id, Long idUsuarioAutenticado){
         Despesa despesa = despesaRepository
            .findByIdAndUsuarioId(id, idUsuarioAutenticado)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Despesa não encontrada"
            ));

        despesaRepository.delete(despesa);
    }

//----------------------------------------------------------------------------------------------------

    public List<SugestaoAgendamentoDTO> sugestaoDespesas(Long usuarioId){

        LocalDate hoje = LocalDate.now();
        
        LocalDateTime inicioPeriodo = hoje
            .minusMonths(4)
            .withDayOfMonth(1)
            .atStartOfDay();

        LocalDateTime fimPeriodo = hoje
            .plusMonths(1)
            .withDayOfMonth(
                hoje.plusMonths(1).lengthOfMonth()
            )
            .atTime(23, 59, 59);

        LocalDate proximoMes = hoje.plusMonths(1);

        Map<Long, List<HistoricoDespesaProjection>> agrupadoPorCategoria = buscarHistoricoAgrupado(usuarioId, 
            inicioPeriodo, 
            fimPeriodo);
        
        List<SugestaoAgendamentoDTO> sugestoes = new ArrayList<>();
        
        for(Map.Entry<Long, List<HistoricoDespesaProjection>> entry : agrupadoPorCategoria.entrySet()){

            List<HistoricoDespesaProjection> registroDaCategoria = entry.getValue();

            LocalDateTime dataSugestao = null;
            
            HistoricoDespesaProjection referencia = buscarReferencia(registroDaCategoria, proximoMes);

            if (referencia == null) {
                 continue;
            }

            if(!possuiDespesaNesteMes(registroDaCategoria, hoje)){
                dataSugestao = calcularDataSugestao(referencia, hoje);
            }else if (!possuiDespesaProximoMes(registroDaCategoria, proximoMes)) {
                dataSugestao =  calcularDataSugestao(referencia);
            }else{
                continue;
            }

            BigDecimal media = calcularMedia(dataSugestao, registroDaCategoria);

            if(media == null) continue;

            sugestoes.add(criarSugestaoAgendamentoDTO(referencia, 
                entry.getKey(), 
                registroDaCategoria.get(0).getCategoriaNome(),
                dataSugestao,
                media
                ));
        }   
        return sugestoes;
    }
    
    //to quebrando o laço for em varios metodos =)

    private boolean possuiDespesaNesteMes(List<HistoricoDespesaProjection> registroDaCategoria, LocalDate hoje){
        return registroDaCategoria.stream()
                .anyMatch(r -> r.getData().getYear() == hoje.getYear()
                    && r.getData().getMonthValue() == hoje.getMonthValue());
    }
    
    private boolean possuiDespesaProximoMes(List<HistoricoDespesaProjection> registroDaCategoria, LocalDate proximoMes){
        return registroDaCategoria.stream()
                .anyMatch(r ->
                    r.getData().getYear() == proximoMes.getYear()
                    && r.getData().getMonthValue() == proximoMes.getMonthValue());
    }

    private HistoricoDespesaProjection buscarReferencia(List<HistoricoDespesaProjection> registroDaCategoria, LocalDate proximoMes){
        return registroDaCategoria.stream()
                .filter(r ->
                    r.getData().getYear() != proximoMes.getYear()
                    || r.getData().getMonthValue() != proximoMes.getMonthValue()
                )
                .max(Comparator.comparing(HistoricoDespesaProjection::getData))
                .orElse(null);
    }

    private LocalDateTime calcularDataSugestao(HistoricoDespesaProjection referencia, LocalDate hoje){
        int diaOriginal = referencia.getData().getDayOfMonth();

        YearMonth mesDestino =
                YearMonth.of(hoje.getYear(), hoje.getMonth());

        int ultimoDia = mesDestino.lengthOfMonth();

        int diaValido = Math.min(diaOriginal, ultimoDia);

        return referencia.getData()
                .withYear(hoje.getYear())
                .withMonth(hoje.getMonthValue())
                .withDayOfMonth(diaValido);
    }

    private LocalDateTime calcularDataSugestao(HistoricoDespesaProjection referencia){
        return referencia.getData().plusMonths(1);
    }

    private BigDecimal calcularMedia(LocalDateTime dataSugestao, List<HistoricoDespesaProjection> registroDaCategoria){
        final LocalDateTime dataSugestaoFinal = dataSugestao;
            YearMonth mesSugestao = YearMonth.from(dataSugestaoFinal);

            List<HistoricoDespesaProjection> ultimosCinco = registroDaCategoria.stream()
                .filter(r ->
                    YearMonth.from(r.getData()).isBefore(mesSugestao)
                 )
                .sorted(Comparator.comparing(HistoricoDespesaProjection::getData).reversed())
                .limit(5)
                .toList();

            BigDecimal somaTotal = ultimosCinco.stream()
                .map(HistoricoDespesaProjection::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


            int quantidadeRegistros = ultimosCinco.size();
            if (quantidadeRegistros == 0) {
                return null;
            }
        return somaTotal.divide(
                BigDecimal.valueOf(quantidadeRegistros),
                2,
                RoundingMode.HALF_UP
            );
    }

    private Map<Long, List<HistoricoDespesaProjection>> buscarHistoricoAgrupado(Long usuarioId, LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo){
        List<HistoricoDespesaProjection> historico = despesaRepository.buscarHistoricoFixas(usuarioId, inicioPeriodo, fimPeriodo);
        if(historico.isEmpty()){
            throw new ResourceNotFoundException("Nenhuma despesa fixa encontrada");
        }
        return historico.stream()
            .collect(Collectors.groupingBy(HistoricoDespesaProjection::getCategoriaId));
    }

    private SugestaoAgendamentoDTO criarSugestaoAgendamentoDTO(HistoricoDespesaProjection referencia, Long categoriaId, String categoriaNome, LocalDateTime dataSugestao, BigDecimal media){
        return new SugestaoAgendamentoDTO(referencia.getDescricao(),
                                                     categoriaId, 
                                                     categoriaNome,
                                                     dataSugestao ,
                                                     media,
                                                     "FIXA",
                                                     "SUGESTAO");
    }
    //scheduler
    @Transactional
    public int atualizarAgendadasVencidas() {
        LocalDateTime agora = LocalDateTime.now();
        return despesaRepository.atualizarAgendadasVencidas(agora);
    }

    //conversão e regras de negocio
    private DespesaResponseDTO converterParaResponseDTO(Despesa despesa) {
        return DespesaResponseDTO.builder()
            .id(despesa.getId())
            .descricao(despesa.getDescricao())
            .valor(despesa.getValor())
            .data(despesa.getData())
            .recorrencia(despesa.getRecorrencia())
            .idCategoria(despesa.getCategoria().getId())
            .status(despesa.getStatus())
            .build();
    }

    private void verificarCategoria(Categoria categoria){
        if (!"DESPESA".equals(categoria.getTipo())) {
            throw new IllegalArgumentException("Esta categoria pertence ao mundo das receitas, não pode ser usada em despesas.");
        }
    }

    private void validarDataAgendamento(DespesaRequestDTO dto){
        LocalDateTime agora = LocalDateTime.now();
        if (dto.getStatus() == StatusTransacao.AGENDADA && !dto.getData().isAfter(agora)){
            throw new AgendarDespesaPassadoException("Não é possivel agendar uma despesa no passado");
        }
    }
}
