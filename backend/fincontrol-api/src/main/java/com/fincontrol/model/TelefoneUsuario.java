package com.fincontrol.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="telefone_usuario")
@Data
@NoArgsConstructor
public class TelefoneUsuario {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_telefone")
    private Long id;

    @Column(name="numero", length=15, nullable = false)
    private String telefone;

    @Column(name="id_usuario", nullable = false)
    private Long idUsuario;

    public interface telefoneProjection{
        String getTelefone();
        Long getId();
    }

    public TelefoneUsuario(String telefone, Long idUsuario){
        this.telefone = telefone;
        this.idUsuario = idUsuario;
    }
}


