package com.fincontrol.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String nome;
    
    private String senhaAtual;

    private String senhaNova;

    @Email
    private String email;

}
