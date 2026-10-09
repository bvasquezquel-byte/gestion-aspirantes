package edu.ingsoft.bvasquez.quel.repository;

import edu.ingsoft.bvasquez.quel.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    boolean existsByNombreUsuario(String nombreUsuario);
    void save(Usuario usuario);
}
