package com.fincontrol.dto.user;

import java.util.List;

import com.fincontrol.model.TelefoneUsuario.telefoneProjection;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO {

    @NotBlank
    private String nome;
    
    @NotBlank
    @Email
    private String email;

    private List<telefoneProjection> telefones;
}
