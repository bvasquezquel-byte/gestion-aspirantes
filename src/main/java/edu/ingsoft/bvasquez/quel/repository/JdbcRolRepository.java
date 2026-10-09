package edu.ingsoft.bvasquez.quel.repository;

import edu.ingsoft.bvasquez.quel.config.DatabaseConfig;
import edu.ingsoft.bvasquez.quel.exception.DataAccessException;
import edu.ingsoft.bvasquez.quel.model.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class JdbcRolRepository implements RolRepository {

    private final DatabaseConfig db;

    public JdbcRolRepository(DatabaseConfig db) {
        this.db = db;
    }

    @Override
    public List<Rol> findAll() {
        return Arrays.asList(Rol.values());
    }

    @Override
    public Optional<Rol> findById(int id) {
        String sql = "SELECT id_rol, nombre FROM rol WHERE id_rol = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nombreRol = rs.getString("nombre");
                    if ("RECLUTADOR".equalsIgnoreCase(nombreRol) || "Usuario".equalsIgnoreCase(nombreRol) || "USER".equalsIgnoreCase(nombreRol)) {
                        return Optional.of(Rol.USER);
                    }
                    return Optional.of(Rol.ADMIN);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo consultar el rol.", e);
        }
    }
}