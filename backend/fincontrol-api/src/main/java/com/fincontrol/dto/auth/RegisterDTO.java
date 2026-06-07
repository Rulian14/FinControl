package com.fincontrol.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterDTO {
    
    @NotBlank
    @Size(max=100, message="voce é por acaso o Laurence Gregory Watkins?")
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=10, max=15)
    private String senha;
}
