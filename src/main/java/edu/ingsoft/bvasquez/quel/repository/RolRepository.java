package edu.ingsoft.bvasquez.quel.repository;

import edu.ingsoft.bvasquez.quel.model.Rol;
import java.util.List;
import java.util.Optional;

public interface RolRepository {
    List<Rol> findAll();
    Optional<Rol> findById(int id);
}
