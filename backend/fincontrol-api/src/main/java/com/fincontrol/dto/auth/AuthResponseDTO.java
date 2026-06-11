package com.fincontrol.dto.auth;


import com.fincontrol.dto.user.UserResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AuthResponseDTO {

    private String token;
    
    private String tipo;

    private long expiration;

    private UserResponseDTO userResponseDTO;
}
