package edu.ingsoft.bvasquez.quel.model;

public enum Rol {
    ADMIN("Administrador"),
    USER("Usuario");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}