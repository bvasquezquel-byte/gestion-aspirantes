package edu.ingsoft.bvasquez.quel.dto;

/** id = 0 cuando el aspirante aún no existe en la base de datos. */
public record AspiranteDTO(int id, String nombre, String apellido, String correo,
                           String telefono, String direccion, int edad) { }
