package br.com.educavrv.application.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import br.com.educavrv.domain.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService {

    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        if (usuario.email == null || usuario.email.isEmpty() || usuario.senha == null) {
            throw new IllegalArgumentException("Email e senha são obrigatórios.");
        }

        // Criptografa a senha antes de salvar
        usuario.senha = BcryptUtil.bcryptHash(usuario.senha);

        usuario.persist();
        return usuario;
    }

    public Optional<Usuario> autenticar(String email, String senha) {
        Usuario usuario = Usuario.find("email", email).firstResult();

        if (usuario != null) {
            // Compara a senha fornecida com o hash armazenado no BD
            if (BcryptUtil.matches(senha, usuario.senha)) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }
}
