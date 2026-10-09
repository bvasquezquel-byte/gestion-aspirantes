package edu.ingsoft.bvasquez.quel.model;

public class Aspirante {
    private Integer id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;
    private Integer edad;

    public Aspirante() {
    }

    public Aspirante(Integer id, String nombre, String apellido, String correo, String telefono, String direccion, Integer edad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.edad = edad;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
}