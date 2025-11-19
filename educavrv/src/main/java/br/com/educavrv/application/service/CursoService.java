package br.com.educavrv.application.service;

import br.com.educavrv.domain.model.Curso;
import br.com.educavrv.domain.model.Area;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class CursoService {

    // Regra de Negócio/Validação e Criação (CREATE)
    @Transactional
    public Curso criarNovoCurso(Curso curso) {
        if (curso.nome == null || curso.nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do curso é obrigatório.");
        }
        if (curso.cargaHoraria <= 0) {
            throw new IllegalArgumentException("A carga horária deve ser positiva.");
        }
        // Verifique se a Area existe ou lance uma exceção
        if (curso.area != null && curso.area.id != null) {
            curso.area = Area.findById(curso.area.id);
            if (curso.area == null) {
                throw new NotFoundException("Área com ID " + curso.area.id + " não encontrada.");
            }
        }

        curso.persist(); // Salva no banco (CREATE)
        return curso;
    }

    // Leitura (READ)
    public List<Curso> listarTodos() {
        return Curso.listAll();
    }

    // Leitura por ID (READ)
    public Curso buscarPorId(Long id) {
        return Curso.findById(id);
    }

    // Atualização (UPDATE)
    @Transactional
    public Curso atualizarCurso(Long id, Curso dadosAtualizados) {
        Curso cursoExistente = buscarPorId(id);

        if (cursoExistente == null) {
            return null; // Retorna null para o Resource retornar 404
        }

        // Aplica as regras de negócio
        if (dadosAtualizados.cargaHoraria <= 0) {
            throw new IllegalArgumentException("Carga horária deve ser positiva.");
        }

        // Atualiza campos
        cursoExistente.nome = dadosAtualizados.nome;
        cursoExistente.descricao = dadosAtualizados.descricao;
        cursoExistente.cargaHoraria = dadosAtualizados.cargaHoraria;

        // Se a área for atualizada, buscar a nova referência
        if (dadosAtualizados.area != null && dadosAtualizados.area.id != null) {
            Area novaArea = Area.findById(dadosAtualizados.area.id);
            if (novaArea == null) {
                throw new NotFoundException("Nova Área com ID " + dadosAtualizados.area.id + " não encontrada.");
            }
            cursoExistente.area = novaArea;
        }

        return cursoExistente; // O Panache persiste as mudanças ao fim do @Transactional
    }

    // Exclusão (DELETE)
    @Transactional
    public boolean deletarCurso(Long id) {
        return Curso.deleteById(id);
    }
}
