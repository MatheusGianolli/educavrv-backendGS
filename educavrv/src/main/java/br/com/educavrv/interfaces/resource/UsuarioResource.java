package br.com.educavrv.interfaces.resource;

import br.com.educavrv.domain.model.Usuario;
import br.com.educavrv.application.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/api/v1/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    // Endpoint: POST /api/v1/usuarios/cadastro
    @POST
    @Path("/cadastro")
    public Response cadastrar(Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.criarUsuario(usuario);
            return Response.status(Response.Status.CREATED).entity(novoUsuario).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // Endpoint: POST /api/v1/usuarios/login
    @POST
    @Path("/login")
    public Response login(Usuario credenciais) {
        Optional<Usuario> usuario = usuarioService.autenticar(credenciais.email, credenciais.senha);

        if (usuario.isPresent()) {
            // Boa Prática: Nunca retorne a senha, mesmo que hasheada
            usuario.get().senha = null;
            return Response.ok(usuario.get()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas.").build();
        }
    }
}