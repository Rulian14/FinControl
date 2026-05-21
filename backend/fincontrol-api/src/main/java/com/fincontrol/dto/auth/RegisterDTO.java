package com.fincontrol.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=10)
    private String senha;
}
