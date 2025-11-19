package br.com.educavrv.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Area extends PanacheEntity {
    public String nome; // Ex: "Saude", "Educacao"

    public Area() {}

    // Construtor auxiliar para testes/inicialização
    public Area(String nome) {
        this.nome = nome;
    }
}
