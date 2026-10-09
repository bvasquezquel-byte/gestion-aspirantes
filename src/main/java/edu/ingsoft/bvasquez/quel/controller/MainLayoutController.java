package edu.ingsoft.bvasquez.quel.controller;

import edu.ingsoft.bvasquez.quel.dto.UsuarioDTO;
import edu.ingsoft.bvasquez.quel.exception.AppException;
import edu.ingsoft.bvasquez.quel.service.AuthService;
import edu.ingsoft.bvasquez.quel.util.AlertHelper;
import edu.ingsoft.bvasquez.quel.util.SceneManager;
import edu.ingsoft.bvasquez.quel.util.SceneManager.View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML private StackPane contentPane;
    @FXML private Label lblUsuario;

    private final AuthService authService;
    private final SceneManager sceneManager;

    public MainLayoutController(AuthService authService, SceneManager sceneManager) {
        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @FXML
    private void initialize() {
        sceneManager.setContentHost(contentPane);
        UsuarioDTO usuario = authService.getUsuarioActual();
        lblUsuario.setText(usuario.nombreUsuario() + " (" + usuario.rol() + ")");
        mostrar(View.DASHBOARD);
    }

    @FXML private void onDashboard() { mostrar(View.DASHBOARD); }
    @FXML private void onAspirantes() { mostrar(View.ASPIRANTES); }

    @FXML
    private void onLogout() {
        authService.logout();
        sceneManager.switchTo(View.LOGIN);
    }

    private void mostrar(View view) {
        try {
            sceneManager.showInContent(view);
        } catch (AppException e) {
            AlertHelper.error("Error", e.getMessage());
        }
    }
}
