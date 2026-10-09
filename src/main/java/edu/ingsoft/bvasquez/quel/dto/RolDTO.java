package edu.ingsoft.bvasquez.quel.dto;

public record RolDTO(int id, String nombre) {
    @Override public String toString() { return nombre; }
}
