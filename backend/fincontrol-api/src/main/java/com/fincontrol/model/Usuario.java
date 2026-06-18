package com.fincontrol.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String email;
    private String senhaHash;
    private LocalDateTime dataCriacao;

    public Usuario(String nome, String email, String senhaHash, LocalDateTime dataCriacao) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCriacao = dataCriacao;
    }

}
