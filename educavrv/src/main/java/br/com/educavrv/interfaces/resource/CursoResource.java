package br.com.educavrv.interfaces.resource;

import br.com.educavrv.domain.model.Curso;
import br.com.educavrv.application.service.CursoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/v1/cursos") // URI: Caminho do Recurso
@Produces(MediaType.APPLICATION_JSON) // Retorna JSON
@Consumes(MediaType.APPLICATION_JSON) // Recebe JSON
public class CursoResource {

    @Inject
    CursoService cursoService; // Injeção da Camada BO/Service

    // Requisito: Seguir princípios REST (POST para criar)
    // POST /api/v1/cursos
    @POST
    public Response criar(Curso curso) {
        try {
            Curso novoCurso = cursoService.criarNovoCurso(curso);
            // Código de Status: 201 Created
            return Response.status(Response.Status.CREATED).entity(novoCurso).build();
        } catch (IllegalArgumentException | NotFoundException e) {
            // Código de Status: 400 Bad Request (Validação falhou ou FK não existe)
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Requisito: Seguir princípios REST (GET para listar/ler)
    // GET /api/v1/cursos
    @GET
    public List<Curso> listar() {
        // Código de Status: 200 OK (implícito pelo JAX-RS ao retornar a lista)
        return cursoService.listarTodos();
    }

    // GET /api/v1/cursos/{id}
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Curso curso = cursoService.buscarPorId(id);

        if (curso == null) {
            // Código de Status: 404 Not Found
            return Response.status(Response.Status.NOT_FOUND).entity("Curso não encontrado.").build();
        }
        // Código de Status: 200 OK
        return Response.ok(curso).build();
    }

    // Requisito: Seguir princípios REST (PUT para atualizar)
    // PUT /api/v1/cursos/{id}
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, Curso curso) {
        try {
            Curso cursoAtualizado = cursoService.atualizarCurso(id, curso);

            if (cursoAtualizado == null) {
                return Response.status(Response.Status.NOT_FOUND).build(); // 404
            }
            // Código de Status: 200 OK
            return Response.ok(cursoAtualizado).build();
        } catch (IllegalArgumentException | NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build(); // 400
        }
    }

    // Requisito: Seguir princípios REST (DELETE para excluir)
    // DELETE /api/v1/cursos/{id}
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        boolean deletado = cursoService.deletarCurso(id);

        if (deletado) {
            // Código de Status: 204 No Content (Sucesso na exclusão sem corpo de resposta)
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404
        }
    }

    // FALTA: Configuração CORS (Geralmente no application.properties, mas pode ser feito com @Provider)
}
