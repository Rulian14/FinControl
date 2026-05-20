package com.fincontrol.dto.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDTO {

    @NotBlank
    private String token;
    
    @NotBlank
    private String tipo;

    private long expiration;
}
