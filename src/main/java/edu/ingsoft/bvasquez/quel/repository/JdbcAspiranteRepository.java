package edu.ingsoft.bvasquez.quel.repository;

import edu.ingsoft.bvasquez.quel.config.DatabaseConfig;
import edu.ingsoft.bvasquez.quel.exception.DataAccessException;
import edu.ingsoft.bvasquez.quel.model.Aspirante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcAspiranteRepository implements AspiranteRepository {

    private final DatabaseConfig db;

    public JdbcAspiranteRepository(DatabaseConfig db) {
        this.db = db;
    }

    @Override
    public List<Aspirante> findAll() {
        String sql = "SELECT id_aspirante, nombre, apellido, correo, telefono, direccion, edad "
                   + "FROM aspirante ORDER BY id_aspirante";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Aspirante> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new Aspirante(
                        rs.getInt("id_aspirante"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getInt("edad")
                ));
            }
            return lista;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo obtener la lista de aspirantes.", e);
        }
    }

    @Override
    public void save(Aspirante a) {
        String sql = "INSERT INTO aspirante (nombre, apellido, correo, telefono, direccion, edad) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getCorreo());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getDireccion());
            ps.setInt(6, a.getEdad());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo guardar el aspirante.", e);
        }
    }

    @Override
    public boolean update(Aspirante a) {
        String sql = "UPDATE aspirante SET nombre = ?, apellido = ?, correo = ?, telefono = ?, "
                   + "direccion = ?, edad = ? WHERE id_aspirante = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setString(3, a.getCorreo());
            ps.setString(4, a.getTelefono());
            ps.setString(5, a.getDireccion());
            ps.setInt(6, a.getEdad());
            ps.setInt(7, a.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo actualizar el aspirante.", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM aspirante WHERE id_aspirante = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo eliminar el aspirante.", e);
        }
    }

    @Override
    public boolean existsByCorreo(String correo, int excluirId) {
        String sql = "SELECT COUNT(*) FROM aspirante WHERE correo = ? AND id_aspirante <> ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setInt(2, excluirId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo verificar el correo.", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM aspirante";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new DataAccessException("No se pudo contar los aspirantes.", e);
        }
    }
}