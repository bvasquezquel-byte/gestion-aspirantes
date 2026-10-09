package edu.ingsoft.bvasquez.quel.repository;

import edu.ingsoft.bvasquez.quel.config.DatabaseConfig;
import edu.ingsoft.bvasquez.quel.exception.DataAccessException;
import edu.ingsoft.bvasquez.quel.model.Rol;
import edu.ingsoft.bvasquez.quel.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcUsuarioRepository implements UsuarioRepository {

    private final DatabaseConfig db;

    public JdbcUsuarioRepository(DatabaseConfig db) {
        this.db = db;
    }

    @Override
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT u.id_usuario, u.nombre_usuario, u.password_hash, r.nombre "
                   + "FROM usuario u JOIN rol r ON r.id_rol = u.id_rol WHERE u.nombre_usuario = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                
                // Mapeo del String de la BD al enum Rol
                String nombreRol = rs.getString("nombre");
                Rol rol = Rol.ADMIN;
                if ("Usuario".equalsIgnoreCase(nombreRol) || "USER".equalsIgnoreCase(nombreRol)) {
                    rol = Rol.USER;
                }

                return Optional.of(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("password_hash"),
                        rol
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo consultar el usuario.", e);
        }
    }

    @Override
    public boolean existsByNombreUsuario(String nombreUsuario) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE nombre_usuario = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo verificar el usuario.", e);
        }
    }

    @Override
    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuario (nombre_usuario, password_hash, id_rol) VALUES (?, ?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Asigna id_rol según el enum (1 para ADMIN, 2 para USER)
            int idRol = (usuario.getRol() == Rol.ADMIN) ? 1 : 2;

            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPasswordHash());
            ps.setInt(3, idRol);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo registrar el usuario.", e);
        }
    }
}