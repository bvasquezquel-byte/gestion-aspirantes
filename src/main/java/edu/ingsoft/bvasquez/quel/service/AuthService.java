package edu.ingsoft.bvasquez.quel.service;

import edu.ingsoft.bvasquez.quel.dto.LoginDTO;
import edu.ingsoft.bvasquez.quel.dto.RegistroDTO;
import edu.ingsoft.bvasquez.quel.dto.RolDTO;
import edu.ingsoft.bvasquez.quel.dto.UsuarioDTO;
import edu.ingsoft.bvasquez.quel.exception.AuthException;
import edu.ingsoft.bvasquez.quel.exception.ValidationException;
import edu.ingsoft.bvasquez.quel.model.Rol;
import edu.ingsoft.bvasquez.quel.model.Usuario;
import edu.ingsoft.bvasquez.quel.repository.RolRepository;
import edu.ingsoft.bvasquez.quel.repository.UsuarioRepository;
import edu.ingsoft.bvasquez.quel.security.PasswordHasher;
import edu.ingsoft.bvasquez.quel.security.SessionManager;

import java.util.List;

public class AuthService {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final String CREDENCIALES_INVALIDAS = "Usuario o contraseña incorrectos.";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordHasher passwordHasher;
    private final SessionManager sessionManager;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                       PasswordHasher passwordHasher, SessionManager sessionManager) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordHasher = passwordHasher;
        this.sessionManager = sessionManager;
    }

    public UsuarioDTO login(LoginDTO dto) {
        if (estaVacio(dto.usuario()) || estaVacio(dto.password())) {
            throw new ValidationException("Ingrese su usuario y contraseña.");
        }
        Usuario usuario = usuarioRepository.findByNombreUsuario(dto.usuario().trim())
                .orElseThrow(() -> new AuthException(CREDENCIALES_INVALIDAS));
        if (!passwordHasher.matches(dto.password(), usuario.getPasswordHash())) {
            throw new AuthException(CREDENCIALES_INVALIDAS);
        }
        UsuarioDTO sesion = new UsuarioDTO(usuario.getId(), usuario.getUsername(), usuario.getRol().getDescripcion());
        sessionManager.iniciar(sesion);
        return sesion;
    }

    public void registrar(RegistroDTO dto) {
        validarRegistro(dto);
        String nombreUsuario = dto.usuario().trim();
        if (usuarioRepository.existsByNombreUsuario(nombreUsuario)) {
            throw new AuthException("El usuario '" + nombreUsuario + "' ya existe.");
        }
        Rol rol = rolRepository.findById(dto.rol().id())
                .orElseThrow(() -> new ValidationException("El rol seleccionado no existe."));
        usuarioRepository.save(new Usuario(0, nombreUsuario, passwordHasher.hash(dto.password()), rol));
    }

    public List<RolDTO> listarRoles() {
        return rolRepository.findAll().stream()
                .map(r -> new RolDTO(r == Rol.ADMIN ? 1 : 2, r.getDescripcion()))
                .toList();
    }

    public UsuarioDTO getUsuarioActual() {
        return sessionManager.getUsuarioActual();
    }

    public void logout() {
        sessionManager.cerrar();
    }

    private void validarRegistro(RegistroDTO dto) {
        if (estaVacio(dto.usuario()) || estaVacio(dto.password()) || estaVacio(dto.confirmacion())) {
            throw new ValidationException("Complete todos los campos.");
        }
        if (dto.rol() == null) {
            throw new ValidationException("Seleccione un rol.");
        }
        if (dto.password().length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException("La contraseña debe tener al menos " + MIN_PASSWORD_LENGTH + " caracteres.");
        }
        if (!dto.password().equals(dto.confirmacion())) {
            throw new ValidationException("Las contraseñas no coinciden.");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}