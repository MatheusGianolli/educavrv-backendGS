package br.com.educavrv.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Curso extends PanacheEntity {
    public String nome;

    @Column(columnDefinition = "TEXT")
    public String descricao;

    public int cargaHoraria;
    public LocalDate dataLancamento = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento com Area
    @JoinColumn(name = "area_id")
    public Area area;

    // Relacionamento com Conteúdo (pode ser usado para listar as aulas)
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Conteudo> conteudos;
}
