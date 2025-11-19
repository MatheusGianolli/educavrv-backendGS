package br.com.educavrv.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
public class Usuario extends PanacheEntity {
    public String nome;

    @Column(unique = true) // Garantir que o email seja único
    public String email;

    public String senha; // Armazenar HASH (Lembre-se da Boa Prática!)
    public String tipo;  // "ALUNO" ou "ADMIN"

    // Getters e Setters simplificados (Panache cuida de grande parte)
}


