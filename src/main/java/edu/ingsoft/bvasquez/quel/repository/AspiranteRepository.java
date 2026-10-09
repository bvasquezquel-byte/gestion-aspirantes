package edu.ingsoft.bvasquez.quel.repository;

import edu.ingsoft.bvasquez.quel.model.Aspirante;
import java.util.List;

public interface AspiranteRepository {
    List<Aspirante> findAll();
    void save(Aspirante aspirante);
    boolean update(Aspirante aspirante);
    boolean delete(int id);
    boolean existsByCorreo(String correo, int excluirId);
    long count();
}