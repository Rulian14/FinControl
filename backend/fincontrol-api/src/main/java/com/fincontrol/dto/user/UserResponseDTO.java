package com.fincontrol.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    @NotBlank
    private long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

}
