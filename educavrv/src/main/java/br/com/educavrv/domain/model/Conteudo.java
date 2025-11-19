package br.com.educavrv.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Conteudo extends PanacheEntity {

    public String titulo;
    public String urlVideo; // URL do vídeo/aula
    public String tipo;     // Ex: "AULA", "QUIZ", "DOCUMENTO"
    public int duracaoMinutos;

    // Relacionamento 1:N (Muitos Conteúdos pertencem a 1 Curso)
    @ManyToOne
    @JoinColumn(name = "curso_id") // Chave estrangeira
    public Curso curso;

    // Getters e Setters simplificados
}
