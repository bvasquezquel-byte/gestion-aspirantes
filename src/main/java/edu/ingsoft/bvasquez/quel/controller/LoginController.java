package edu.ingsoft.bvasquez.quel.controller;

import edu.ingsoft.bvasquez.quel.dto.LoginDTO;
import edu.ingsoft.bvasquez.quel.exception.AppException;
import edu.ingsoft.bvasquez.quel.exception.AuthException;
import edu.ingsoft.bvasquez.quel.service.AuthService;
import edu.ingsoft.bvasquez.quel.util.AlertHelper;
import edu.ingsoft.bvasquez.quel.util.SceneManager;
import edu.ingsoft.bvasquez.quel.util.SceneManager.View;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    private final AuthService authService;
    private final SceneManager sceneManager;

    public LoginController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @FXML
    private void onLogin() {
        try {
            authService.login(new LoginDTO(txtUsuario.getText(), txtPassword.getText()));
            sceneManager.switchTo(View.MAIN_LAYOUT);
        } catch (AuthException e) {
            AlertHelper.error("Acceso denegado", e.getMessage());
            txtPassword.clear();
        } catch (AppException e) {
            AlertHelper.warning("No se pudo iniciar sesión", e.getMessage());
        }
    }

    @FXML
    private void onIrRegistro() {
        sceneManager.switchTo(View.REGISTER);
    }
}
