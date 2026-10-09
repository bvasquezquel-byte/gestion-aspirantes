package edu.ingsoft.bvasquez.quel.controller;

import edu.ingsoft.bvasquez.quel.dto.RegistroDTO;
import edu.ingsoft.bvasquez.quel.dto.RolDTO;
import edu.ingsoft.bvasquez.quel.exception.AppException;
import edu.ingsoft.bvasquez.quel.service.AuthService;
import edu.ingsoft.bvasquez.quel.util.AlertHelper;
import edu.ingsoft.bvasquez.quel.util.SceneManager;
import edu.ingsoft.bvasquez.quel.util.SceneManager.View;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmacion;
    @FXML private ComboBox<RolDTO> cmbRol;

    private final AuthService authService;
    private final SceneManager sceneManager;

    public RegisterController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @FXML
    private void initialize() {
        try {
            cmbRol.getItems().setAll(authService.listarRoles());
        } catch (AppException e) {
            AlertHelper.error("Error", e.getMessage());
        }
    }

    @FXML
    private void onRegistrar() {
        try {
            authService.registrar(new RegistroDTO(txtUsuario.getText(), txtPassword.getText(),
                    txtConfirmacion.getText(), cmbRol.getValue()));
            AlertHelper.info("Registro exitoso", "Usuario creado. Ya puede iniciar sesión.");
            sceneManager.switchTo(View.LOGIN);
        } catch (AppException e) {
            AlertHelper.warning("No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void onVolver() {
        sceneManager.switchTo(View.LOGIN);
    }
}
