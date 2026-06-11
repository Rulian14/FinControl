package com.fincontrol.dto.meta;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaRequestDTO {

    @NotBlank
    private String nome;

    @NotNull
    @Min(1)
    private BigDecimal valorObjetivo;

    @NotNull
    @Min(0)
    private BigDecimal valorContribuido;

    @NotNull
    @Future
    private LocalDate dataLimite;
}
