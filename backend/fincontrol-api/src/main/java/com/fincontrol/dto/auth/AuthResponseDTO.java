package com.fincontrol.dto.auth;


import com.fincontrol.dto.user.UserResponseDTO;

import jakarta.validation.constraints.NotBlank;

public class AuthResponseDTO {

    @NotBlank
    private String token;
    
    @NotBlank
    private String tipo;

    private UserResponseDTO user;
}
