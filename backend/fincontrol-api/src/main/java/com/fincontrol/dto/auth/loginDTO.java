package com.fincontrol.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO{

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min=10)
    private String senha;
    
    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

} 
