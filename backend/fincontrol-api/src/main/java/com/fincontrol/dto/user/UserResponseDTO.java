package com.fincontrol.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@AllArgsConstructor
public class UserResponseDTO {

    @NotBlank
    private long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

}
