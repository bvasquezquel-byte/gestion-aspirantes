package edu.ingsoft.bvasquez.quel.controller;

import edu.ingsoft.bvasquez.quel.dto.AspiranteDTO;
import edu.ingsoft.bvasquez.quel.exception.AppException;
import edu.ingsoft.bvasquez.quel.exception.ValidationException;
import edu.ingsoft.bvasquez.quel.service.AspiranteService;
import edu.ingsoft.bvasquez.quel.util.AlertHelper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AspiranteController {

    @FXML private TableView<AspiranteDTO> tblAspirantes;
    @FXML private TableColumn<AspiranteDTO, Integer> colId;
    @FXML private TableColumn<AspiranteDTO, String> colNombre;
    @FXML private TableColumn<AspiranteDTO, String> colApellido;
    @FXML private TableColumn<AspiranteDTO, String> colCorreo;
    @FXML private TableColumn<AspiranteDTO, String> colTelefono;
    @FXML private TableColumn<AspiranteDTO, String> colDireccion;
    @FXML private TableColumn<AspiranteDTO, Integer> colEdad;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtEdad;

    private final AspiranteService aspiranteService;
    private final ObservableList<AspiranteDTO> aspirantes = FXCollections.observableArrayList();
    private AspiranteDTO seleccionado;

    public AspiranteController(AspiranteService aspiranteService) {
        this.aspiranteService = aspiranteService;
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().id()));
        colNombre.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().nombre()));
        colApellido.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().apellido()));
        colCorreo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().correo()));
        colTelefono.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().telefono()));
        colDireccion.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().direccion()));
        colEdad.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().edad()));
        tblAspirantes.setItems(aspirantes);
        tblAspirantes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                seleccionado = actual;
                cargarFormulario(actual);
            }
        });
        refrescarTabla();
    }

    @FXML
    private void onAgregar() {
        ejecutar(() -> {
            aspiranteService.agregar(leerFormulario(0));
            AlertHelper.info("Aspirante agregado", "El aspirante se guardó correctamente.");
        });
    }

    @FXML
    private void onActualizar() {
        ejecutar(() -> {
            if (seleccionado == null) {
                throw new ValidationException("Seleccione un aspirante de la tabla.");
            }
            aspiranteService.actualizar(leerFormulario(seleccionado.id()));
            AlertHelper.info("Aspirante actualizado", "Los datos se actualizaron correctamente.");
        });
    }

    @FXML
    private void onEliminar() {
        ejecutar(() -> {
            if (seleccionado == null) {
                throw new ValidationException("Seleccione un aspirante de la tabla.");
            }
            String nombreCompleto = seleccionado.nombre() + " " + seleccionado.apellido();
            if (!AlertHelper.confirmar("Eliminar aspirante", "¿Eliminar a " + nombreCompleto + "?")) {
                return;
            }
            aspiranteService.eliminar(seleccionado.id());
        });
    }

    @FXML
    private void onLimpiar() {
        limpiarFormulario();
    }

    /** Ejecuta una acción, refresca la tabla y traduce las excepciones del dominio a Alerts. */
    private void ejecutar(Runnable accion) {
        try {
            accion.run();
            refrescarTabla();
            limpiarFormulario();
        } catch (ValidationException e) {
            AlertHelper.warning("Datos inválidos", e.getMessage());
        } catch (AppException e) {
            AlertHelper.error("Error", e.getMessage());
        }
    }

    private void refrescarTabla() {
        try {
            aspirantes.setAll(aspiranteService.listar());
        } catch (AppException e) {
            AlertHelper.error("Error", e.getMessage());
        }
    }

    private AspiranteDTO leerFormulario(int id) {
        return new AspiranteDTO(id, txtNombre.getText(), txtApellido.getText(), txtCorreo.getText(),
                txtTelefono.getText(), txtDireccion.getText(), parsearEdad());
    }

    private int parsearEdad() {
        try {
            return Integer.parseInt(txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("La edad debe ser un número entero.");
        }
    }

    private void cargarFormulario(AspiranteDTO a) {
        txtNombre.setText(a.nombre());
        txtApellido.setText(a.apellido());
        txtCorreo.setText(a.correo());
        txtTelefono.setText(a.telefono());
        txtDireccion.setText(a.direccion());
        txtEdad.setText(String.valueOf(a.edad()));
    }

    private void limpiarFormulario() {
        seleccionado = null;
        tblAspirantes.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEdad.clear();
    }
}
