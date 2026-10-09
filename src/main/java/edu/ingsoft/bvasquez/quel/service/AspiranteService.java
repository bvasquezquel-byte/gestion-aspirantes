package edu.ingsoft.bvasquez.quel.service;

import edu.ingsoft.bvasquez.quel.dto.AspiranteDTO;
import edu.ingsoft.bvasquez.quel.exception.AspiranteNotFoundException;
import edu.ingsoft.bvasquez.quel.exception.ValidationException;
import edu.ingsoft.bvasquez.quel.model.Aspirante;
import edu.ingsoft.bvasquez.quel.repository.AspiranteRepository;

import java.util.List;
import java.util.regex.Pattern;

public class AspiranteService {

    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern TELEFONO = Pattern.compile("^[0-9+\\-\\s]{8,15}$");
    private static final int EDAD_MINIMA = 18;
    private static final int EDAD_MAXIMA = 100;

    private final AspiranteRepository repository;

    public AspiranteService(AspiranteRepository repository) {
        this.repository = repository;
    }

    public List<AspiranteDTO> listar() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public void agregar(AspiranteDTO dto) {
        Aspirante aspirante = validarYNormalizar(dto, 0);
        repository.save(aspirante);
    }

    public void actualizar(AspiranteDTO dto) {
        if (dto.id() <= 0) {
            throw new ValidationException("Seleccione un aspirante para actualizar.");
        }
        Aspirante aspirante = validarYNormalizar(dto, dto.id());
        if (!repository.update(aspirante)) {
            throw new AspiranteNotFoundException("El aspirante ya no existe.");
        }
    }

    public void eliminar(int id) {
        if (!repository.delete(id)) {
            throw new AspiranteNotFoundException("El aspirante ya no existe.");
        }
    }

    private Aspirante validarYNormalizar(AspiranteDTO dto, int id) {
        String nombre = requerido(dto.nombre(), "El nombre");
        String apellido = requerido(dto.apellido(), "El apellido");
        String correo = requerido(dto.correo(), "El correo");
        String telefono = requerido(dto.telefono(), "El teléfono");
        String direccion = requerido(dto.direccion(), "La dirección");

        if (!CORREO.matcher(correo).matches()) {
            throw new ValidationException("El correo no tiene un formato válido.");
        }
        if (!TELEFONO.matcher(telefono).matches()) {
            throw new ValidationException("El teléfono debe tener entre 8 y 15 dígitos.");
        }
        if (dto.edad() < EDAD_MINIMA || dto.edad() > EDAD_MAXIMA) {
            throw new ValidationException("La edad debe estar entre " + EDAD_MINIMA + " y " + EDAD_MAXIMA + ".");
        }
        if (repository.existsByCorreo(correo, id)) {
            throw new ValidationException("Ya existe un aspirante con ese correo.");
        }
        return new Aspirante(id, nombre, apellido, correo, telefono, direccion, dto.edad());
    }

    private String requerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ValidationException(campo + " es obligatorio.");
        }
        return valor.trim();
    }

    private AspiranteDTO toDto(Aspirante a) {
        return new AspiranteDTO(
                a.getId(),
                a.getNombre(),
                a.getApellido(),
                a.getCorreo(),
                a.getTelefono(),
                a.getDireccion(),
                a.getEdad()
        );
    }
}