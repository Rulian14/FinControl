package com.fincontrol.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UsuarioDeletarDTO {
    @NotBlank(message = "A senha atual é obrigatória para confirmar a exclusão.")
    @Size(min=10)
    private String senha;
}
