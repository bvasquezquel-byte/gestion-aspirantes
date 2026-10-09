package edu.ingsoft.bvasquez.quel.security;

import edu.ingsoft.bvasquez.quel.dto.UsuarioDTO;

/** Guarda el usuario autenticado durante la ejecución. */
public class SessionManager {

    private UsuarioDTO usuarioActual;

    public void iniciar(UsuarioDTO usuario) { this.usuarioActual = usuario; }
    public void cerrar() { this.usuarioActual = null; }
    public UsuarioDTO getUsuarioActual() { return usuarioActual; }
}
